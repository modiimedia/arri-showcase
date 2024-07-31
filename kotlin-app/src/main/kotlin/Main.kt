import io.ktor.client.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.runBlocking

fun main() {
    val httpClient = HttpClient() {
        install(HttpTimeout)
    }
    val client = MyClient(
        httpClient,
        "http://localhost:3000",
        headers = { -> mutableMapOf() },
    )
    runBlocking {
        val result = client.sayHello(SayHelloParams("John"))
        println(result.message)
    }
}
