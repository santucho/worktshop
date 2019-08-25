package santucho.`0`

/**
 * Variables:
 * - Inmutables: se declaran como `val` y no pueden ser reasignadas
 * - Mutables: se declaran como `var` y pueden ser reasignadas
 *
 * Manejo de nulls:
 * - La declaración del tipo de la variable puede hacerse con un `?`, esto indica que la misma es nulleable.
 * Al declarar una variable como nulleable, kotlin obliga a checkear la misma antes de utilizarla,
 * o utilizarla de forma segura.
 * */

fun vars() {
    var mutable = "las var son variables mutables"
    println(mutable)
    mutable = "las mismas se pueden reasignar"
    println(mutable)
}

fun vals() {
    val inmutable = "las val son variables inmutables"
//    inmutable = "no se pueden reasignar"
    println(inmutable)
}

fun main() {
    vars()
    vals()
}
