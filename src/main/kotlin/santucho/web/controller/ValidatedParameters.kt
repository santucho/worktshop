package santucho.web.controller

import arrow.core.*
import santucho.errors.ApiError
import santucho.errors.RequestError
import spark.Request

fun Request.getParameter(name: String): Validated<RequestError, String> =
    validateParameter(name, this.params(name))

fun Request.getQueryParam(name: String): Validated<RequestError, String> =
    validateParameter(name, this.queryParams(name))

fun Request.getOptionalQueryParam(name: String): Validated<RequestError, Option<String>> = Option.fromNullable(this.queryParams(name)).valid()

fun Request.getCommaSeparatedQueryParam(name: String): Validated<RequestError, NonEmptyList<String>> =
    this.getParameter(name).map { NonEmptyList.fromListUnsafe(it.split(",")) }

fun Nel<RequestError>.handleValidationErrors() =
    ApiError.InvalidParametersError(this.all.joinToString(", ")).left()

private fun validateParameter(name: String, value: String?): Validated<RequestError, String> =
    Option.fromNullable(value).filter { it.isNotEmpty() }.fold(
        { RequestError.MissingParam(name).invalid() },
        { v -> v.valid() }
    )
