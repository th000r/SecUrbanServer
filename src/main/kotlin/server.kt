import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.*
import kotlinx.html.*
import java.text.DateFormat

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

data class Report(
    val userId: String,
    val message: String,
    val location: Boolean,
    val latitude: Double,
    val longitude: Double,
    val picture: Boolean,
    val source: String
)

data class Config(val targetIp: String, val targetPort: Int)

val reportStorage = mutableMapOf<String, Report>()

fun main() {
    val objectMapper = jacksonObjectMapper()
    val configContent = {}.javaClass.getResource("/config.json").readText()
    val config = objectMapper.readValue(configContent, Config::class.java)

    embeddedServer(Netty, port = config.targetPort, host = config.targetIp) {
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            get("/report/{reportId}") {
                val reportId = call.parameters["reportId"]
                val report = reportStorage[reportId]
                if (report != null) {
                    call.respondText("$report", status = HttpStatusCode.OK)
                } else {
                    call.respondText("No report for id $reportId.", status = HttpStatusCode.NotFound)
                }
            }
            get("/report") {
                call.respondText("${reportStorage.keys}")
            }
            post("/report") {
                val report = call.receive<Report>()
                val reportId = Integer.toHexString(report.hashCode())
                reportStorage[reportId] = report
                call.respondText("Received report $report and saved with the id $reportId.", status = HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}