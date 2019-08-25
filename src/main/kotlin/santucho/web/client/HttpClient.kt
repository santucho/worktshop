package santucho.web.client

import arrow.core.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpStatus
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.*
import org.apache.http.impl.client.CloseableHttpClient
import org.slf4j.LoggerFactory
import santucho.errors.*
import java.net.SocketTimeoutException

class HttpClient(
    val clientName: String,
    private val client: CloseableHttpClient,
    private val requestConfig: RequestConfig,
    private val mapper: ObjectMapper
) {

    fun execute(context: HttpRequestContext): Either<ApiError, HttpResponse> {
        val httpRequest = this getRequestFrom context
        // TODO See IO<Any>{}.attempt().unsafeRunSync()
        // TODO See Either.catch { client.execute(httpRequest) }
        // https://github.com/arrow-kt/arrow/issues/1644
        return Try<CloseableHttpResponse> { client.execute(httpRequest) }.toEither()
            .mapLeft { handleExecuteError(it, context) }
            .flatMap { response ->
                if (response.isError())
                    handleResponseError(response, context)
                else
                    response.right()
            }
            .flatMap { response: CloseableHttpResponse -> useResponse(response, context) }
    }

    private fun useResponse(
        response: CloseableHttpResponse,
        context: HttpRequestContext
    ): Either<ApiError, HttpResponse> {
        return response.use {
            if (response.entity != null) {
                Try { response.entity.content.readBytes() }
                .toEither { handleReadError(it, context) }
                .map { body ->
                    HttpResponse(mapper, context.uri,
                        response.statusLine.statusCode,
                        response.allHeaders.toList(),
                        body)
                }
            } else {
                handleEmptyResponse(context)
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

    private fun handleExecuteError(ex: Throwable, context: HttpRequestContext): ApiError {
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
        response: CloseableHttpResponse,
        context: HttpRequestContext
    ): Either<ApiError, Nothing> {
        val apiEx = response.use {
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
        logger.error("${apiEx.message}: ${context.uri}")
        return apiEx.toApiError().left()
    }

    private fun handleReadError(ex: Throwable, context: HttpRequestContext): ApiError {
        val apiError = ApiError.HttpReadResponseError(cause = "Error reading $clientName response")
        logger.error("Error reading $clientName response of ${context.uri}", ex)
        return apiError
    }

    private fun handleEmptyResponse(context: HttpRequestContext): Either<ApiError, Nothing> {
        val apiEx = HttpEmptyResponseException("$clientName response has no content")
        logger.error("Empty $clientName response when requesting ${context.uri}")
        return apiEx.toApiError().left()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(HttpClient::class.java)
    }
}
