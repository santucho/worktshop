package santucho.web.client.interceptor

import arrow.core.Either
import arrow.core.right
import santucho.errors.ApiError
import santucho.web.client.HttpRequestContext
import santucho.web.client.HttpResponse

open class Interceptor {

    /**
     * Function to perform a functional transformation of the request
     * We strongly suggest not to modify the original request object
     * */
    open fun preHandle(
        httpRequestContext: HttpRequestContext
    ): Either<ApiError, HttpRequestContext> =
        httpRequestContext.right()

    /**
     * Function to perform a functional transformation of the response
     * We strongly suggest not to modify the original response object
     * */
    open fun postHandle(
        httpResponse: HttpResponse
    ): Either<ApiError, HttpResponse> =
        httpResponse.right()
}
