import io.ktor.client.*
import kotlinx.coroutines.runBlocking

fun main() {
    val httpClient = HttpClient()
    val client = MyClient(
        httpClient,
        "http://localhost:3000",
        headers = { -> mutableMapOf() },
    )
    runBlocking {
        client.sayHello(SayHelloParams("John"))
    }
}
