import io.ktor.client.*
import kotlin.concurrent.thread

fun main() {
    val httpClient = HttpClient()
    val client = Client(
        httpClient,
        "http://localhost:3000",
        headers = { -> mutableMapOf() },
    )
    thread {
        client.sayHello("John")
    }
}