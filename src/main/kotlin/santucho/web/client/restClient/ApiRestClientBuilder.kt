package santucho.web.client.restClient

import com.typesafe.config.Config
import santucho.utils.UrlUtils
import santucho.web.client.HttpClient
import santucho.web.client.interceptor.Interceptor
import santucho.web.client.interceptor.Interceptors

class ApiRestClientBuilder(
    private var httpClient: HttpClient,
    private var interceptors: List<Interceptor> = listOf(),
    private var urlUtils: UrlUtils,
    private var config: Config
) {
    fun withInterceptor(interceptor: Interceptor): ApiRestClientBuilder {
        this.interceptors = interceptors + interceptor
        return this
    }

    fun build(): ApiRestClient {
        return ApiRestClient(httpClient, Interceptors(interceptors), urlUtils, config)
    }
}
