package santucho.utils

import com.google.inject.Singleton
import java.net.URI
import org.apache.commons.lang3.StringUtils
import org.apache.http.client.utils.URIBuilder

@Singleton
class UrlUtils {

    fun build(protocol: String, host: String, url: String): URI {
        val uriBuilder = URIBuilder()
            .setScheme(protocol)
            .setHost(host)

        val parts = url.split(Regex("[?]|&"))
        parts.mapIndexed { index, s ->
            when (index) {
                0 -> uriBuilder.setPath(s)
                else -> uriBuilder.setParameter(
                    s.substringBefore("="),
                    s.substringAfter("=", StringUtils.EMPTY).ifBlank { null }
                )
            }
        }

        return uriBuilder.build()
    }
}
