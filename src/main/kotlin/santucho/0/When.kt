package santucho.`0`

fun handleHttpError(httpError: HttpError) = when (httpError) {
    is HttpError.BadRequest -> "Revisa la url, kinga"
    is HttpError.NotFound -> "Donde está el recurso? Acá no"
    is HttpError.InternalServerError -> "Veniamos bien, pero pasaron cosas"
    is HttpError.UnknownError -> "Se rompió la pipa???"
}

fun main() {
    listOf(HttpError.BadRequest, HttpError.NotFound, HttpError.InternalServerError, HttpError.UnknownError(""))
        .forEach { println(handleHttpError(it)) }
}