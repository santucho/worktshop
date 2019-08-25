package santucho.web.client

import arrow.core.Either
import arrow.core.Try
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import org.apache.http.Header
import org.slf4j.LoggerFactory
import santucho.errors.ApiError

class HttpResponse(
    val mapper: ObjectMapper,
    val uri: URI,
    val status: Int,
    val headers: List<Header>,
    val body: ByteArray
) {
    fun <T> getResponseAs(typeReference: TypeReference<T>): Either<ApiError, T> {
        return Try {
            mapper.readValue<T>(body, typeReference)
        }.toEither { handleParseError(it, uri) }
    }

    fun <T> parsedWith(f: (ByteArray) -> Try<T>): Either<ApiError, T> {
        return f(body)
            .toEither { handleParseError(it, uri) }
    }

    private fun handleParseError(ex: Throwable, uri: URI): ApiError {
        val apiError = ApiError.ParseError(cause = "Error deserializing body")
        logger.error("Error deserializing response of $uri", ex)
        return apiError
    }

    companion object {
        private val logger = LoggerFactory.getLogger(HttpResponse::class.java)
    }
}
