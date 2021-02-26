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

data class Report(val citizenId: String, val message: String, val locationData: Boolean, val picture: Boolean)


val reportStorage = mutableMapOf<String, Report>()

const val targetIp = "127.0.0.1" //set to proper value while testing TODO: make adjustable for proper release

fun main() {
    embeddedServer(Netty, port = 8080, host = targetIp) {
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