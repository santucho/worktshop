package santucho.error

import org.apache.http.HttpStatus

/**
 * We centralize application errors in a sealed class
 * */
sealed class ApiError(
    val code: Int = HttpStatus.SC_INTERNAL_SERVER_ERROR,
    val message: String = "Oops! Something went wrong",
    val cause: String
) {
    class NotFound(code: Int = 404, cause: String) : ApiError(code = code, cause = cause)
    class InternalServerError(code: Int = 500, cause: String) : ApiError(code = code, cause = cause)
}
