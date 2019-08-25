package santucho.web.client

import com.google.inject.Inject
import com.google.inject.Singleton
import com.typesafe.config.Config
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.Registry
import org.apache.http.config.RegistryBuilder
import org.apache.http.config.SocketConfig
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.protocol.HttpRequestExecutor
import org.apache.http.ssl.SSLContexts
import santucho.utils.ObjectMapperProvider
import javax.net.ssl.HostnameVerifier

@Singleton
class HttpClientCreator @Inject constructor(
    private val config: Config
) {

    fun createClient(client: String): HttpClient {
        val restConfig = config.getConfig("$client.$REST_CONNECTOR")

        val requestConfig = buildStandardRequestConfig(restConfig)

        val httpClient = HttpClients
            .custom()
            .setConnectionManager(buildConnectionManager(restConfig))
            .setRequestExecutor(HttpRequestExecutor())
            .setRetryHandler(DefaultHttpRequestRetryHandler(restConfig.getInt(MAX_RETRIES), false))
            .setDefaultRequestConfig(requestConfig)
            .build()

        return HttpClient(
            clientName = client, client = httpClient,
            requestConfig = requestConfig, mapper = ObjectMapperProvider.mapper
        )
    }

    private fun buildStandardRequestConfig(config: Config): RequestConfig {
        return RequestConfig
            .copy(RequestConfig.DEFAULT)
            .setMaxRedirects(3)
            .setConnectionRequestTimeout(config.getInt(CONNECTION_TIMEOUT))
            .setConnectTimeout(config.getInt(CONNECTION_TIMEOUT))
            .setSocketTimeout(config.getInt(READ_TIMEOUT))
            .build()
    }

    private fun buildConnectionManager(config: Config): PoolingHttpClientConnectionManager {
        val connManager = PoolingHttpClientConnectionManager(buildConnectionSocketFactoryRegistry())

        connManager.defaultSocketConfig = buildSocketConfig(config)
        connManager.validateAfterInactivity = config.getInt(IDLE_CONNECTION_TIMEOUT)
        connManager.maxTotal = config.getInt(MAX_CONNECTIONS)
        connManager.defaultMaxPerRoute = config.getInt(MAX_CONNECTIONS)

        return connManager
    }

    private fun buildConnectionSocketFactoryRegistry(): Registry<ConnectionSocketFactory> {
        val registryBuilder = RegistryBuilder.create<ConnectionSocketFactory>()
        val sslContext = SSLContexts.createDefault()
        val hostnameVerifier = getHostnameVerifier()
        registryBuilder.register(HTTPS, SSLConnectionSocketFactory(sslContext, hostnameVerifier))
        registryBuilder.register(HTTP, PlainConnectionSocketFactory.INSTANCE)
        return registryBuilder.build()
    }

    private fun getHostnameVerifier(): HostnameVerifier {
        return SSLConnectionSocketFactory.getDefaultHostnameVerifier()
    }

    private fun buildSocketConfig(config: Config): SocketConfig {
        return SocketConfig
            .custom()
            .setTcpNoDelay(true)
            .setSoKeepAlive(true)
            .setSoTimeout(config.getInt(READ_TIMEOUT))
            .build()
    }

    companion object {
        const val REST_CONNECTOR = "rest-connector"
        const val HTTPS = "https"
        const val HTTP = "http"
        const val MAX_RETRIES = "request.max-retries"
        const val READ_TIMEOUT = "read-timeout"
        const val CONNECTION_TIMEOUT = "connection-timeout"
        const val IDLE_CONNECTION_TIMEOUT = "idle-connection-timeout"
        const val MAX_CONNECTIONS = "max-connections"
    }
}
