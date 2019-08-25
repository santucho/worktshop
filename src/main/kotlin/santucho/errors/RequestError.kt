package santucho.errors

sealed class RequestError(val field: String) {
    class MissingParam(field: String) : RequestError(field) {
        override fun toString(): String =
            "missing $field parameter"
    }

    class WrongParamValue(field: String) : RequestError(field) {
        override fun toString(): String =
            "wrong value in $field parameter"
    }
}
