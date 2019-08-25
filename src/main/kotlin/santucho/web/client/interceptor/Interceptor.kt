package santucho.web.client.interceptor

import santucho.web.client.HttpRequestContext
import santucho.web.client.HttpResponse

open class Interceptor {

    /**
     * We strongly suggest not to modify the original request object
     * */
    open fun preHandle(httpRequestContext: HttpRequestContext): HttpRequestContext = httpRequestContext

    /**
     * We strongly suggest not to modify the original response object
     * */
    open fun postHandle(httpResponse: HttpResponse): HttpResponse = httpResponse
}
