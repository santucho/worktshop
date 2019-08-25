package santucho.`1`


/**
 * https://kotlinlang.org/docs/reference/scope-functions.html
 *
 * Scope Functions - let, run, with, apply & also
 *
 * Son funciones que ejecutan un bloque de código sobre un objeto,
 * variando entre una y otra la forma en la que de disponibiliza el bloque de objeto dentro del bloque,
 * así como también el resultado del mismo
 *
 * Referencia al objeto:
 * - run, with y apply referencian al objeto como `this`
 * - let y also referencia al objeto como `it`
 *
 * Valor retornado
 * - let, run y with devuelven el resultado del bloque/lambda
 * - apply y also devuelven el objeto en cuestión
 * */
fun useLet() {
    val letResult: String = ScopeFunctionModel(
        id = "1",
        type = "MODEL",
        description = "let"
    ).let {
        "${it.id} - ${it.type} - ${it.description}"
    }
}

fun useRun() {
    val useResult: String = ScopeFunctionModel(
        id = "2",
        type = "MODEL",
        description = "run"
    ).run {
        "${this.id} - ${this.type} - ${this.description}"
    }
}

fun useWith() {
    val sfm = ScopeFunctionModel(
        id = "3",
        type = "MODEL",
        description = "with"
    )
    val withResult: String = with(sfm) {
        "${this.id} - ${this.type} - ${this.description}"
    }
}

fun useApply() {
    val letResult: ScopeFunctionModel = ScopeFunctionModel(
        id = "4",
        type = "MODEL",
        description = "use"
    ).apply {
        "${this.id} - ${this.type} - ${this.description}"
    }
}

fun useAlso() {
    val letResult: ScopeFunctionModel = ScopeFunctionModel(
        id = "5",
        type = "MODEL",
        description = "also"
    ).also {
        "${it.id} - ${it.type} - ${it.description}"
    }
}

fun combineWithNullable(sfm: ScopeFunctionModel?) {
    val nullableLetResult = sfm?.let {
        "${it.id} - ${it.type} - ${it.description}"
    }
}

data class ScopeFunctionModel(
    val id: String,
    val type: String,
    val description: String
)