package santucho.web.client.restClient

import com.typesafe.config.Config
import java.net.URI
import santucho.utils.UrlUtils
import santucho.web.client.HttpClient
import santucho.web.client.HttpMethod
import santucho.web.client.HttpRequestContext
import santucho.web.client.HttpResponse
import santucho.web.client.interceptor.Interceptors

class ApiRestClient(
    private val httpClient: HttpClient,
    private val interceptors: Interceptors,
    private val urlUtils: UrlUtils,
    private val config: Config
) {

    fun get(
        url: String,
        customHeaders: Map<String, String> = mapOf()
    ): HttpResponse {
        return interceptors.interceptAndExecute(
            httpClient, HttpRequestContext(HttpMethod.GET, buildUriWith(url), customHeaders = customHeaders)
        )
    }

    private fun buildUriWith(url: String): URI {
        val protocol = config.getString("${httpClient.clientName}.protocol")
        val host = config.getString("${httpClient.clientName}.host")
        return urlUtils.build(protocol, host, url)
    }
}
