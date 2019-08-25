package santucho.web.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import org.apache.http.Header
import org.slf4j.LoggerFactory
import santucho.errors.ParseException

class HttpResponse(
    val mapper: ObjectMapper,
    val uri: URI,
    val status: Int,
    val headers: List<Header>,
    val body: ByteArray
) {
    fun <T> getResponseAs(typeReference: TypeReference<T>): T {
        return try {
            mapper.readValue<T>(body, typeReference)
        } catch (ex: Throwable) {
            val pe = ParseException("Error deserializing body", ex)
            logger.error("Error deserializing response of $uri", pe)
            throw pe
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(HttpResponse::class.java)
    }
}
