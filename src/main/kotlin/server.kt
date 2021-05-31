import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

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

const val INSERT_STATEMENT = "insert into reports (user_id,message,location,latitude,longitude,picture,source) " +
        "values (?,?,?,?,?,?,?)";

@Serializable
data class Report(
    val userId: String,
    val message: String,
    val location: Boolean,
    val latitude: Double,
    val longitude: Double,
    val picture: Boolean,
    val source: String
)

data class Config(
    val targetIp: String,
    val targetPort: Int,
    val dbUserName: String,
    val dbPassword: String,
    val dbName: String
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
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            post("/report") {
                val report = objectMapper.readValue(call.receiveText(), Report::class.java)
                connection?.run {
                    enterReportIntoDB(report, connection)
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

fun enterReportIntoDB(report: Report, connection: Connection) =
    with(connection.prepareStatement(INSERT_STATEMENT)) {
        setString(1, report.userId)
        setString(2, report.message)
        setBoolean(3, report.location)
        setDouble(4, report.latitude)
        setDouble(5, report.longitude)
        setBoolean(6, report.picture)
        setString(7, report.source)
        executeUpdate()
    }