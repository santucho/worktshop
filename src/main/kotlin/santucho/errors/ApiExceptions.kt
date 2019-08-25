package santucho.errors

import org.apache.http.HttpStatus

abstract class ApiException(val errorMessage: String, val statusCode: Int, cause: Throwable? = null) : RuntimeException(errorMessage, cause) {
    abstract fun toApiError(): ApiError
}

/**
 * Commons
 * */
class ParseException(errorMessage: String, cause: Throwable) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR) {
    override fun toApiError(): ApiError = ApiError.ParseError(errorMessage)
}

/**
 * HttpClient
 * */
class BadRequestException(statusCode: Int = 400, errorMessage: String) : ApiException(errorMessage, statusCode) {
    override fun toApiError(): ApiError = ApiError.BadRequest(statusCode, errorMessage)
}

class NotFoundException(statusCode: Int, errorMessage: String) : ApiException(errorMessage, statusCode) {
    override fun toApiError(): ApiError = ApiError.NotFound(statusCode, errorMessage)
}

class InternalServerErrorException(statusCode: Int, errorMessage: String) : ApiException(errorMessage, statusCode) {
    override fun toApiError(): ApiError = ApiError.InternalServerError(statusCode, errorMessage)
}

class HttpStatusException(statusCode: Int, errorMessage: String) : ApiException(errorMessage, statusCode) {
    override fun toApiError(): ApiError = ApiError.HttpStatusError(statusCode, errorMessage)
}

class HttpEmptyResponseException(errorMessage: String) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR) {
    override fun toApiError(): ApiError = ApiError.HttpEmptyResponse(errorMessage)
}

class HttpTimeoutException(errorMessage: String, cause: Throwable) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR, cause) {
    override fun toApiError(): ApiError = ApiError.HttpTimeout(errorMessage)
}

class HttpExecuteException(errorMessage: String, cause: Throwable) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR, cause) {
    override fun toApiError(): ApiError = ApiError.HttpExecuteError(errorMessage)
}

class HttpNoResponseException(errorMessage: String) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR) {
    override fun toApiError(): ApiError = ApiError.HttpEmptyResponse(errorMessage)
}

class HttpReadResponseException(errorMessage: String) : ApiException(errorMessage, HttpStatus.SC_INTERNAL_SERVER_ERROR) {
    override fun toApiError(): ApiError = ApiError.HttpReadResponseError(errorMessage)
}

fun IllegalArgumentException.toApiError(): ApiError.IllegalArgument = ApiError.IllegalArgument(this.message ?: "")
