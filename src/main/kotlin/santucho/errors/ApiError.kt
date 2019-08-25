package santucho.errors

import org.apache.http.HttpStatus

sealed class ApiError(
    val code: Int = HttpStatus.SC_INTERNAL_SERVER_ERROR,
    val message: String = "Oops! Something went wrong",
    val cause: String
) {
    /**
     * Commons
     * */
    class ParseError(cause: String) : ApiError(cause = cause)

    /**
     * Controller / ControllerAdvisor
     * */
    class IllegalArgument(cause: String) : ApiError(code = HttpStatus.SC_BAD_REQUEST, cause = cause)
    class InvalidParametersError(cause: String) : ApiError(code = HttpStatus.SC_BAD_REQUEST, cause = cause, message = cause)
    class UnknownError(cause: String) : ApiError(cause = cause)

    /**
     * HttpClient
     * */
    class BadRequest(code: Int, cause: String) : ApiError(code = code, cause = cause)
    class NotFound(code: Int, cause: String) : ApiError(code = code, cause = cause)
    class InternalServerError(code: Int, cause: String) : ApiError(code = code, cause = cause)
    class HttpStatusError(code: Int, cause: String) : ApiError(code = code, cause = cause)
    class HttpTimeout(cause: String) : ApiError(cause = cause)
    class HttpExecuteError(cause: String) : ApiError(cause = cause)
    class HttpReadResponseError(cause: String) : ApiError(cause = cause)
    class HttpEmptyResponse(cause: String) : ApiError(cause = cause)
    class HttpNoResponse(cause: String) : ApiError(cause = cause)
}
