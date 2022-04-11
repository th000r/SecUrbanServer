import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.sql.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.random.Random


// for random strings
private val char_pool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
private const val MAX_BYTE_UPLOAD_SIZE = 10490000

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor with Netty"
        }
    }
}

const val INSERT_REPORT_STATEMENT =
        "insert into reports (user_id, message, location, latitude, longitude, picture, source, timestamp) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?)"

const val INSERT_REPORT_IMAGE_STATEMENT =
        "insert into report_images (report_id, image_name) " +
        "values (?, ?)"

@Serializable
data class Report(
    val userId: String,
    val message: String,
    val location: Boolean,
    val latitude: Double,
    val longitude: Double,
    val picture: Boolean,
    val source: String,
    val timestamp: String
)

data class Config(
    val targetIp: String,
    val targetPort: Int,
    val dbUserName: String,
    val dbPassword: String,
    val dbName: String
)

data class ReportImage(
    val part_name: String?,
    val file_bytes: ByteArray
)

fun main() {
    val objectMapper = jacksonObjectMapper()
    val configContent = {}.javaClass.getResource("/config.json").readText()
    val config = objectMapper.readValue(configContent, Config::class.java)
    val connection = getDatabaseConnection(config.dbUserName, config.dbPassword, config.dbName)

    embeddedServer(Netty, port = config.targetPort, host = config.targetIp) {
        install(ContentNegotiation) {
            json(Json{
                prettyPrint = true
                isLenient = true
            })
        }

        install(DoubleReceive)

        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }

            post("/api/report") {
                // val report = objectMapper.readValue(call.receiveText(), Report::class.java)
                var report_id: Int = 0
                var report: Report? = null
                var report_images: MutableList<ReportImage> = mutableListOf()
                var upload_size = 0

                // start transaction
                connection?.autoCommit = false
                connection?.run {
                    val multipart = call.receiveMultipart()

                    multipart.forEachPart { part ->
                        if (part is PartData.FormItem) {
                            val rep = objectMapper.readValue(part.value, Report::class.java)
                            val rep_id = insertIntoReport(rep, connection)
                            report = rep
                            report_id = rep_id
                        }

                        if (part is PartData.FileItem) {
                            val file_bytes = part.streamProvider().readBytes()
                            upload_size += file_bytes.size
                            println(file_bytes.size)
                            report_images.add(ReportImage(part.name, file_bytes))
                        }
                        // make sure to dispose of the part after use to prevent leaks
                        part.dispose()
                    }

                    if (upload_size > MAX_BYTE_UPLOAD_SIZE) {
                        System.err.println("Exeeded max upload size ($upload_size / $MAX_BYTE_UPLOAD_SIZE ) bytes.")
                        call.respond(HttpStatusCode.PayloadTooLarge, "Exceeded max upload size")
                        return@post
                    }

                    for (img in report_images) {
                        val name = createImageName(img.part_name)
                        File("upload/report/$name").writeBytes(img.file_bytes)
                        insertIntoReportImages(report_id, name, connection)
                    }

                    // finish transaction
                    connection.commit()
                    call.respond(HttpStatusCode.Created)
                } ?: run {
                    System.err.println("DB connection missing. Failed to enter report $report")
                    call.respond(HttpStatusCode.InternalServerError, "DB connection missing")
                }
            }
        }
    }.start(wait = true)
}

fun getDatabaseConnection(username: String, password: String, dbName: String): Connection? =
    try {
        DriverManager.getConnection("jdbc:mysql://localhost:3306/$dbName", username, password)
    } catch (ex: SQLException) {
        ex.printStackTrace()
        null
    }

fun insertIntoReport(report: Report, connection: Connection): Int =
    with(connection.prepareStatement(INSERT_REPORT_STATEMENT, Statement.RETURN_GENERATED_KEYS)) {
        setString(1, report.userId)
        setString(2, report.message)
        setBoolean(3, report.location)
        setDouble(4, report.latitude)
        setDouble(5, report.longitude)
        setBoolean(6, report.picture)
        setString(7, report.source)
        setTimestamp(8, Timestamp.valueOf(report.timestamp))
        executeUpdate()

        val rs = generatedKeys
        if (rs.first()) {
            val id = rs.getInt(1)
            // System.out.printf("id of new report : %d", id)
            return id
        } else {
            throw SQLException("No keys generated for report")
        }
    }

fun insertIntoReportImages(report_id: Int, image_name: String, connection: Connection) =
    with(connection.prepareStatement(INSERT_REPORT_IMAGE_STATEMENT)) {
        setInt(1, report_id)
        setString(2, image_name)
        executeUpdate()
    }

// creates a random string of specified length
fun randomString(length: Int): String {
    return (1..length)
        .map { i -> Random.nextInt(0, char_pool.size) }
        .map(char_pool::get)
        .joinToString("")
}

// returns random image name with timestamp
fun createImageName(part_name: String?): String {
    val extension = part_name?.substring(part_name.lastIndexOf("."));
    val timestamp = DateTimeFormatter
        .ofPattern("yyyy-MM-dd_HH-mm-ss")
        .withZone(ZoneOffset.UTC)
        .format(Instant.now()).toString()

    val randomString = randomString(6)

    return timestamp.plus("_").plus(randomString).plus(extension)
}