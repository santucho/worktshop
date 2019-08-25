package santucho.web.client.interceptor

import santucho.web.client.HttpClient
import santucho.web.client.HttpRequestContext
import santucho.web.client.HttpResponse

class Interceptors(private val interceptors: List<Interceptor>) {

    fun interceptAndExecute(
        httpClient: HttpClient,
        httpRequestContext: HttpRequestContext
    ): HttpResponse {
        val interceptedRequest = preHandle(httpRequestContext)
        val response = httpClient.execute(interceptedRequest)
        return postHandle(response)
    }

    private fun preHandle(httpRequestContext: HttpRequestContext): HttpRequestContext {
        return this.interceptors.fold(httpRequestContext) { request, interceptor ->
            interceptor.preHandle(request)
        }
    }

    private fun postHandle(httpResponse: HttpResponse): HttpResponse {
        return this.interceptors.fold(httpResponse) { response: HttpResponse, interceptor ->
            interceptor.postHandle(response)
        }
    }
}
