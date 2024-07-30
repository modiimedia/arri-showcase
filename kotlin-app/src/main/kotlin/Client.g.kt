@file:Suppress(
    "FunctionName", "LocalVariableName", "UNNECESSARY_NOT_NULL_ASSERTION", "ClassName", "NAME_SHADOWING",
    "USELESS_IS_CHECK", "unused", "RemoveRedundantQualifierName", "CanBeParameter", "RedundantUnitReturnType",
    "RedundantExplicitType"
)

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.nio.ByteBuffer
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val generatedClientVersion = "1"
private val timestampFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.ofOffset("GMT", ZoneOffset.UTC))
private val JsonInstance = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
private typealias headersFn = (() -> MutableMap<String, String>?)?

class MyClient(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val headers: headersFn,
) {
    suspend fun sayHello(params: SayHelloParams): SayHelloResponse {
        val response = __prepareRequest(
            client = httpClient,
            url = "$baseUrl/say-hello",
            method = HttpMethod.Get,
            params = params,
            headers = headers?.invoke(),
        ).execute()
        if (response.headers["Content-Type"] != "application/json") {
            throw MyClientError(
                code = 0,
                errorMessage = "Expected server to return Content-Type \"application/json\". Got \"${response.headers["Content-Type"]}\"",
                data = JsonPrimitive(response.bodyAsText()),
                stack = null,
            )
        }
        if (response.status.value in 200..299) {
            return SayHelloResponse.fromJson(response.bodyAsText())
        }
        throw MyClientError.fromJson(response.bodyAsText())
    }
}



interface MyClientModel {
    fun toJson(): String
    fun toUrlQueryParams(): String
}

interface MyClientModelFactory<T> {
    fun new(): T
    fun fromJson(input: String): T
    fun fromJsonElement(
        __input: JsonElement,
        instancePath: String = "",
    ): T
}

data class MyClientError(
    val code: Int,
    val errorMessage: String,
    val data: JsonElement?,
    val stack: List<String>?,
) : Exception(errorMessage), MyClientModel {
    override fun toJson(): String {
        var output = "{"
        output += "\"code\":"
        output += "$code"
        output += ",\"message\":"
        output += buildString { printQuoted(errorMessage) }
        if (data != null) {
            output += ",\"data\":"
            output += JsonInstance.encodeToString(data)
        }
        if (stack != null) {
            output += ",\"stack\":"
            output += "["
            for ((__index, __element) in stack.withIndex()) {
                if (__index > 0) {
                    output += ","
                }
                output += buildString { printQuoted(__element) }
            }
            output += "]"
        }
        output += "}"
        return output
    }

    override fun toUrlQueryParams(): String {
        val queryParts = mutableListOf<String>()
        queryParts.add("code=${code}")
        queryParts.add("message=${errorMessage}")
        return queryParts.joinToString("&")
    }

    companion object Factory : MyClientModelFactory<MyClientError> {
        override fun new(): MyClientError {
            return MyClientError(
                code = 0,
                errorMessage = "",
                data = null,
                stack = null
            )
        }

        override fun fromJson(input: String): MyClientError {
            return fromJsonElement(JsonInstance.parseToJsonElement(input))
        }

        override fun fromJsonElement(__input: JsonElement, instancePath: String): MyClientError {
            if (__input !is JsonObject) {
                __logError("[WARNING] MyClientError.fromJsonElement() expected JsonObject at $instancePath. Got ${__input.javaClass}. Initializing empty MyClientError.")
            }
            val code = when (__input.jsonObject["code"]) {
                is JsonPrimitive -> __input.jsonObject["code"]!!.jsonPrimitive.intOrNull ?: 0
                else -> 0
            }
            val errorMessage = when (__input.jsonObject["message"]) {
                is JsonPrimitive -> __input.jsonObject["message"]!!.jsonPrimitive.contentOrNull ?: ""
                else -> ""
            }
            val data = when (__input.jsonObject["data"]) {
                is JsonNull -> null
                is JsonElement -> __input.jsonObject["data"]!!
                else -> null
            }
            val stack = when (__input.jsonObject["stack"]) {
                is JsonArray -> {
                    val stackVal = mutableListOf<String>()
                    for (item in __input.jsonObject["stack"]!!.jsonArray) {
                        stackVal.add(
                            when (item) {
                                is JsonPrimitive -> item.contentOrNull ?: ""
                                else -> ""
                            }
                        )
                    }
                    stackVal
                }

                else -> null

            }
            return MyClientError(
                code,
                errorMessage,
                data,
                stack,
            )
        }

    }
}

data class SayHelloParams(
    val name: String,
) : MyClientModel {
    override fun toJson(): String {
var output = "{"
output += "\"name\":"
output += buildString { printQuoted(name) }
output += "}"
return output    
    }

    override fun toUrlQueryParams(): String {
val queryParts = mutableListOf<String>()
queryParts.add("name=$name")
return queryParts.joinToString("&")
    }

    companion object Factory : MyClientModelFactory<SayHelloParams> {
        @JvmStatic
        override fun new(): SayHelloParams {
            return SayHelloParams(
                name = "",
            )
        }

        @JvmStatic
        override fun fromJson(input: String): SayHelloParams {
            return fromJsonElement(JsonInstance.parseToJsonElement(input))
        }

        @JvmStatic
        override fun fromJsonElement(__input: JsonElement, instancePath: String): SayHelloParams {
            if (__input !is JsonObject) {
                __logError("[WARNING] SayHelloParams.fromJsonElement() expected kotlinx.serialization.json.JsonObject at $instancePath. Got ${__input.javaClass}. Initializing empty SayHelloParams.")
                return new()
            }
val name: String = when (__input.jsonObject["name"]) {
                is JsonPrimitive -> __input.jsonObject["name"]!!.jsonPrimitive.contentOrNull ?: ""
                else -> ""
            }
            return SayHelloParams(
                name,
            )
        }
    }
}



data class SayHelloResponse(
    val message: String,
) : MyClientModel {
    override fun toJson(): String {
var output = "{"
output += "\"message\":"
output += buildString { printQuoted(message) }
output += "}"
return output    
    }

    override fun toUrlQueryParams(): String {
val queryParts = mutableListOf<String>()
queryParts.add("message=$message")
return queryParts.joinToString("&")
    }

    companion object Factory : MyClientModelFactory<SayHelloResponse> {
        @JvmStatic
        override fun new(): SayHelloResponse {
            return SayHelloResponse(
                message = "",
            )
        }

        @JvmStatic
        override fun fromJson(input: String): SayHelloResponse {
            return fromJsonElement(JsonInstance.parseToJsonElement(input))
        }

        @JvmStatic
        override fun fromJsonElement(__input: JsonElement, instancePath: String): SayHelloResponse {
            if (__input !is JsonObject) {
                __logError("[WARNING] SayHelloResponse.fromJsonElement() expected kotlinx.serialization.json.JsonObject at $instancePath. Got ${__input.javaClass}. Initializing empty SayHelloResponse.")
                return new()
            }
val message: String = when (__input.jsonObject["message"]) {
                is JsonPrimitive -> __input.jsonObject["message"]!!.jsonPrimitive.contentOrNull ?: ""
                else -> ""
            }
            return SayHelloResponse(
                message,
            )
        }
    }
}



// Implementation copied from https://github.com/Kotlin/kotlinx.serialization/blob/d0ae697b9394103879e6c7f836d0f7cf128f4b1e/formats/json/commonMain/src/kotlinx/serialization/json/internal/StringOps.kt#L45
internal const val STRING = '"'

private fun toHexChar(i: Int): Char {
    val d = i and 0xf
    return if (d < 10) (d + '0'.code).toChar()
    else (d - 10 + 'a'.code).toChar()
}

internal val ESCAPE_STRINGS: Array<String?> = arrayOfNulls<String>(93).apply {
    for (c in 0..0x1f) {
        val c1 = toHexChar(c shr 12)
        val c2 = toHexChar(c shr 8)
        val c3 = toHexChar(c shr 4)
        val c4 = toHexChar(c)
        this[c] = "\\u$c1$c2$c3$c4"
    }
    this['"'.code] = "\\\""
    this['\\'.code] = "\\\\"
    this['\t'.code] = "\\t"
    this['\b'.code] = "\\b"
    this['\n'.code] = "\\n"
    this['\r'.code] = "\\r"
    this[0x0c] = "\\f"
}

internal val ESCAPE_MARKERS: ByteArray = ByteArray(93).apply {
    for (c in 0..0x1f) {
        this[c] = 1.toByte()
    }
    this['"'.code] = '"'.code.toByte()
    this['\\'.code] = '\\'.code.toByte()
    this['\t'.code] = 't'.code.toByte()
    this['\b'.code] = 'b'.code.toByte()
    this['\n'.code] = 'n'.code.toByte()
    this['\r'.code] = 'r'.code.toByte()
    this[0x0c] = 'f'.code.toByte()
}

internal fun StringBuilder.printQuoted(value: String) {
    append(STRING)
    var lastPos = 0
    for (i in value.indices) {
        val c = value[i].code
        if (c < ESCAPE_STRINGS.size && ESCAPE_STRINGS[c] != null) {
            append(value, lastPos, i) // flush prev
            append(ESCAPE_STRINGS[c])
            lastPos = i + 1
        }
    }

    if (lastPos != 0) append(value, lastPos, value.length)
    else append(value)
    append(STRING)
}

private fun __logError(string: String) {
    System.err.println(string)
}

private suspend fun __prepareRequest(
    client: HttpClient,
    url: String,
    method: HttpMethod,
    params: MyClientModel?,
    headers: MutableMap<String, String>?,
): HttpStatement {
    var finalUrl = url
    var finalBody = ""
    when (method) {
        HttpMethod.Get, HttpMethod.Head -> {
            finalUrl = "$finalUrl?${params?.toUrlQueryParams() ?: ""}"
        }

        HttpMethod.Post, HttpMethod.Put, HttpMethod.Patch, HttpMethod.Delete -> {
            finalBody = params?.toJson() ?: ""
        }
    }
    val builder = HttpRequestBuilder()
    builder.method = method
    builder.url(finalUrl)
    builder.timeout {
        requestTimeoutMillis = 10 * 60 * 1000
    }
    if (headers != null) {
        for (entry in headers.entries) {
            builder.headers[entry.key] = entry.value
        }
    }
    builder.headers["client-version"] = generatedClientVersion
    if (method != HttpMethod.Get && method != HttpMethod.Head) {
        builder.setBody(finalBody)
    }
    return client.prepareRequest(builder)
}

private fun __parseSseEvent(input: String): __SseEvent {
    val lines = input.split("\n")
    var id: String? = null
    var event: String? = null
    var data: String = ""
    for (line in lines) {
        if (line.startsWith("id: ")) {
            id = line.substring(3).trim()
            continue
        }
        if (line.startsWith("event: ")) {
            event = line.substring(6).trim()
            continue
        }
        if (line.startsWith("data: ")) {
            data = line.substring(5).trim()
            continue
        }
    }
    return __SseEvent(id, event, data)
}

private class __SseEvent(val id: String? = null, val event: String? = null, val data: String)

private class __SseEventParsingResult(val events: List<__SseEvent>, val leftover: String)

private fun __parseSseEvents(input: String): __SseEventParsingResult {
    val inputs = input.split("\n\n").toMutableList()
    if (inputs.isEmpty()) {
        return __SseEventParsingResult(
            events = listOf(),
            leftover = "",
        )
    }
    if (inputs.size == 1) {
        return __SseEventParsingResult(
            events = listOf(),
            leftover = inputs.last(),
        )
    }
    val leftover = inputs.last()
    inputs.removeLast()
    val events = mutableListOf<__SseEvent>()
    for (item in inputs) {
        if (item.contains("data: ")) {
            events.add(__parseSseEvent(item))
        }
    }
    return __SseEventParsingResult(
        events = events,
        leftover = leftover,
    )
}

private suspend fun __handleSseRequest(
    scope: CoroutineScope,
    httpClient: HttpClient,
    url: String,
    method: HttpMethod,
    params: MyClientModel?,
    headers: headersFn,
    backoffTime: Long,
    maxBackoffTime: Long,
    lastEventId: String?,
    onOpen: ((response: HttpResponse) -> Unit) = {},
    onClose: (() -> Unit) = {},
    onError: ((error: MyClientError) -> Unit) = {},
    onData: ((data: String) -> Unit) = {},
    onConnectionError: ((error: MyClientError) -> Unit) = {},
    bufferCapacity: Int,
) {
    val finalHeaders = headers?.invoke() ?: mutableMapOf()
    var lastId = lastEventId
    // exponential backoff maxing out at 32 seconds
    if (backoffTime > 0) {
        withContext(scope.coroutineContext) {
            Thread.sleep(backoffTime)
        }
    }
    var newBackoffTime =
        if (backoffTime == 0L) 2L else if (backoffTime * 2L >= maxBackoffTime) maxBackoffTime else backoffTime * 2L
    if (lastId != null) {
        finalHeaders["Last-Event-ID"] = lastId.toString()
    }
    val request = __prepareRequest(
        client = httpClient,
        url = url,
        method = method,
        params = params,
        headers = finalHeaders,
    )
    try {
        request.execute { httpResponse ->
            try {
                onOpen(httpResponse)
            } catch (e: CancellationException) {
                onClose()
                return@execute
            }
            if (httpResponse.status.value !in 200..299) {
                try {
                    if (httpResponse.headers["Content-Type"] == "application/json") {
                        onConnectionError(
                            MyClientError.fromJson(httpResponse.bodyAsText())
                        )
                    } else {
                        onConnectionError(
                            MyClientError(
                                code = httpResponse.status.value,
                                errorMessage = httpResponse.status.description,
                                data = JsonPrimitive(httpResponse.bodyAsText()),
                                stack = null,
                            )
                        )
                    }
                } catch (e: CancellationException) {
                    onClose()
                    return@execute
                }
                __handleSseRequest(
                    scope = scope,
                    httpClient = httpClient,
                    url = url,
                    method = method,
                    params = params,
                    headers = headers,
                    backoffTime = newBackoffTime,
                    maxBackoffTime = maxBackoffTime,
                    lastEventId = lastId,
                    bufferCapacity = bufferCapacity,
                    onOpen = onOpen,
                    onClose = onClose,
                    onError = onError,
                    onData = onData,
                    onConnectionError = onConnectionError,
                )
                return@execute
            }
            if (httpResponse.headers["Content-Type"] != "text/event-stream") {
                try {
                    onConnectionError(
                        MyClientError(
                            code = 0,
                            errorMessage = "Expected server to return Content-Type \"text/event-stream\". Got \"${httpResponse.headers["Content-Type"]}\"",
                            data = JsonPrimitive(httpResponse.bodyAsText()),
                            stack = null,
                        )
                    )
                } catch (e: CancellationException) {
                    return@execute
                }
                __handleSseRequest(
                    scope = scope,
                    httpClient = httpClient,
                    url = url,
                    method = method,
                    params = params,
                    headers = headers,
                    backoffTime = newBackoffTime,
                    maxBackoffTime = maxBackoffTime,
                    lastEventId = lastId,
                    bufferCapacity = bufferCapacity,
                    onOpen = onOpen,
                    onClose = onClose,
                    onError = onError,
                    onData = onData,
                    onConnectionError = onConnectionError,
                )
                return@execute
            }
            newBackoffTime = 0
            val channel: ByteReadChannel = httpResponse.bodyAsChannel()
            var pendingData = ""
            while (!channel.isClosedForRead) {
                val buffer = ByteBuffer.allocateDirect(bufferCapacity)
                val read = channel.readAvailable(buffer)
                if (read == -1) break
                buffer.flip()
                val input = Charsets.UTF_8.decode(buffer).toString()
                val parsedResult = __parseSseEvents("${pendingData}${input}")
                pendingData = parsedResult.leftover
                for (event in parsedResult.events) {
                    if (event.id != null) {
                        lastId = event.id
                    }
                    when (event.event) {
                        "message" -> {
                            try {
                                onData(event.data)
                            } catch (e: CancellationException) {
                                onClose()
                                return@execute
                            }
                        }

                        "done" -> {
                            onClose()
                            return@execute
                        }

                        "error" -> {
                            val error = MyClientError.fromJson(event.data)
                            try {
                                onError(error)
                            } catch (e: CancellationException) {
                                onClose()
                                return@execute
                            }
                        }

                        else -> {}
                    }
                }
            }
            __handleSseRequest(
                scope = scope,
                httpClient = httpClient,
                url = url,
                method = method,
                params = params,
                headers = headers,
                backoffTime = newBackoffTime,
                maxBackoffTime = maxBackoffTime,
                lastEventId = lastId,
                bufferCapacity = bufferCapacity,
                onOpen = onOpen,
                onClose = onClose,
                onError = onError,
                onData = onData,
                onConnectionError = onConnectionError,
            )
        }
    } catch (e: java.net.ConnectException) {
        onConnectionError(
            MyClientError(
                code = 503,
                errorMessage = if (e.message != null) e.message!! else "Error connecting to $url",
                data = JsonPrimitive(e.toString()),
                stack = e.stackTraceToString().split("\n"),
            )
        )
        __handleSseRequest(
            scope = scope,
            httpClient = httpClient,
            url = url,
            method = method,
            params = params,
            headers = headers,
            backoffTime = newBackoffTime,
            maxBackoffTime = maxBackoffTime,
            lastEventId = lastId,
            bufferCapacity = bufferCapacity,
            onOpen = onOpen,
            onClose = onClose,
            onError = onError,
            onData = onData,
            onConnectionError = onConnectionError,
        )
        return
    } catch (e: Exception) {
        __handleSseRequest(
            scope = scope,
            httpClient = httpClient,
            url = url,
            method = method,
            params = params,
            headers = headers,
            backoffTime = newBackoffTime,
            maxBackoffTime = maxBackoffTime,
            lastEventId = lastId,
            bufferCapacity = bufferCapacity,
            onOpen = onOpen,
            onClose = onClose,
            onError = onError,
            onData = onData,
            onConnectionError = onConnectionError,
        )
    }
}