package santucho.web.client.interceptor

import arrow.core.Either
import arrow.core.extensions.list.foldable.foldLeft
import arrow.core.flatMap
import arrow.core.right
import santucho.errors.ApiError
import santucho.web.client.HttpClient
import santucho.web.client.HttpRequestContext
import santucho.web.client.HttpResponse

class Interceptors(private val interceptors: List<Interceptor>) {

    fun interceptAndExecute(
        httpClient: HttpClient,
        httpRequestContext: HttpRequestContext
    ): Either<ApiError, HttpResponse> {
        return preHandle(httpRequestContext)
            .flatMap { request -> httpClient.execute(request) }
            .flatMap { response -> postHandle(response) }
    }

    private fun preHandle(httpRequestContext: HttpRequestContext): Either<ApiError, HttpRequestContext> {
        return this.interceptors.foldLeft(httpRequestContext.right()) { request: Either<ApiError, HttpRequestContext>, interceptor ->
            request.flatMap { interceptor.preHandle(it) }
        }
    }

    private fun postHandle(httpResponse: HttpResponse): Either<ApiError, HttpResponse> {
        return this.interceptors.foldLeft(httpResponse.right()) { response: Either<ApiError, HttpResponse>, interceptor ->
            response.flatMap { interceptor.postHandle(it) }
        }
    }
}
