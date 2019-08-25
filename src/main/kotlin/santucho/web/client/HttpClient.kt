package santucho.web.client

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.SocketTimeoutException
import org.apache.http.HttpStatus
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpHead
import org.apache.http.client.methods.HttpOptions
import org.apache.http.client.methods.HttpPatch
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.HttpTrace
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.CloseableHttpClient
import org.slf4j.LoggerFactory
import santucho.errors.ApiError
import santucho.errors.ApiException
import santucho.errors.BadRequestException
import santucho.errors.HttpEmptyResponseException
import santucho.errors.HttpExecuteException
import santucho.errors.HttpNoResponseException
import santucho.errors.HttpReadResponseException
import santucho.errors.HttpStatusException
import santucho.errors.HttpTimeoutException
import santucho.errors.InternalServerErrorException
import santucho.errors.NotFoundException

class HttpClient(
    val clientName: String,
    private val client: CloseableHttpClient,
    private val requestConfig: RequestConfig,
    private val mapper: ObjectMapper
) {

    fun execute(context: HttpRequestContext): HttpResponse {
        val httpRequest = this getRequestFrom context
        /**
         * We have to define response outside the try/catch block if we want to use it later,
         * but in doing so we have to declare as variable and nullable
         * */
        var response: CloseableHttpResponse? = null
        try {
            response = client.execute(httpRequest)
        } catch (ex: Throwable) {
            handleExecuteError(ex)
        }
        return response?.let { r ->
            if (r.isError())
                throw handleResponseError(r)
            else
                useResponse(r, context)
        } ?: throw HttpNoResponseException("This should never happen")
    }

    private fun useResponse(
        response: CloseableHttpResponse,
        context: HttpRequestContext
    ): HttpResponse {
        return response.use {
            if (response.entity != null) {
                try {
                    val body = response.entity.content.readBytes()
                    HttpResponse(
                        mapper,
                        context.uri,
                        response.statusLine.statusCode,
                        response.allHeaders.toList(),
                        body
                    )
                } catch (ex: Throwable) {
                    throw HttpReadResponseException("Error reading $clientName response")
                }
            } else {
                throw HttpEmptyResponseException("$clientName response has no content")
            }
        }
    }

    private infix fun getRequestFrom(httpRequestContext: HttpRequestContext): HttpUriRequest {
        val request = when (httpRequestContext.method) {
            HttpMethod.GET -> HttpGet(httpRequestContext.uri)
            HttpMethod.POST -> HttpPost(httpRequestContext.uri)
            HttpMethod.PUT -> HttpPut(httpRequestContext.uri)
            HttpMethod.PATCH -> HttpPatch(httpRequestContext.uri)
            HttpMethod.DELETE -> HttpDelete(httpRequestContext.uri)
            HttpMethod.OPTIONS -> HttpOptions(httpRequestContext.uri)
            HttpMethod.HEAD -> HttpHead(httpRequestContext.uri)
            HttpMethod.TRACE -> HttpTrace(httpRequestContext.uri)
        }
        request.config = requestConfig
        return withHeaders(request, httpRequestContext.customHeaders)
    }

    private fun withHeaders(request: HttpRequestBase, customHeaders: Map<String, String>): HttpUriRequest {
        customHeaders.forEach { (k, v) -> request.setHeader(k, v) }
        return request
    }

    private fun CloseableHttpResponse.isError(): Boolean =
        this.statusCode() in 400..599

    private fun CloseableHttpResponse.statusCode(): Int =
        this.statusLine.statusCode

    private fun CloseableHttpResponse.errorMsg(): String =
        this.statusLine.reasonPhrase

    private fun handleExecuteError(ex: Throwable): ApiError {
        return when (ex) {
            is SocketTimeoutException -> {
                val apiEx = HttpTimeoutException("Timeout requesting service $clientName", ex)
                logger.error("Timeout requesting service $clientName", ex)
                apiEx.toApiError()
            }
            else -> {
                val apiEx = HttpExecuteException("Error executing request to $clientName", ex)
                logger.error("Error executing request to $clientName", ex)
                apiEx.toApiError()
            }
        }
    }

    private fun handleResponseError(
        response: CloseableHttpResponse
    ): ApiException {
        return response.use {
            when (it.statusCode()) {
                HttpStatus.SC_BAD_REQUEST -> {
                    BadRequestException(it.statusCode(), "400 - Bad Request requesting ${clientName.toUpperCase()}")
                }
                HttpStatus.SC_NOT_FOUND -> {
                    NotFoundException(it.statusCode(), "404 - Not Found requesting ${clientName.toUpperCase()}")
                }
                HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
                    InternalServerErrorException(it.statusCode(), "500 - Internal Server Error requesting ${clientName.toUpperCase()}")
                }
                else -> {
                    HttpStatusException(it.statusCode(), "${it.statusCode()} - Error requesting ${clientName.toUpperCase()}: ${it.errorMsg()}")
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(HttpClient::class.java)
    }
}
