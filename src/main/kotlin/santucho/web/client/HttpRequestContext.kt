package santucho.web.client

import java.net.URI

enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE;

    fun acceptsBody(): Boolean {
        return when (this) {
            POST, PUT, PATCH -> true
            else -> false
        }
    }
}

data class HttpRequestContext(
    val method: HttpMethod,
    val uri: URI,
    val customHeaders: Map<String, String> = mapOf(),
    val contentType: String? = null,
    val body: Any? = null
)
