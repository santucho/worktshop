package santucho.`0`

import org.apache.commons.lang3.StringUtils
/**
 * Funciones:
 * - La forma de declarar funciones en kotlin es a través de la utilización de la palabra reservada `fun`
 * - Visibilidad
 *   -> Las funciones por default son `public`, es decir visibles para quien importe el archivo o
 *      "conozca" a la clase que tiene declarada la función.
 *   -> Se pueden declarar como `private`, es modificador de visibilidad se comporta igual que en Java
 *   -> Se pueden declarar como `internal`, dando visibilidad dentro del mismo módulo (es útil al desarrollar libraries)
 * */

/**
 * Unit es el equivalente de Kotlin al void de Java
 * */
fun handleState(someClass: SomeClass, someDataClass: SomeDataClass) {
    println("Mutable state: ${someClass.mutableAndNullableAttribute}")
    someClass.mutableAndNullableAttribute = "b"
    println("Mutable state: ${someClass.mutableAndNullableAttribute}")

    println("Data class a: ${someDataClass.a}, b: ${someDataClass.b}")
    val someDataClassCopy = someDataClass.copy(b = "b copy") // Se copia el objeto modificando el valor de b
    println("Data class a: ${someDataClass.a}, b: ${someDataClass.b}")
    println("Copy Data class a: ${someDataClassCopy.a}, b: ${someDataClassCopy.b}")
}

fun inlineFunction(param: Any) = param

infix fun String.concat(aString: String) = "$this$aString"

fun useInfix() {
    val aString = "a string"
    val otherString = "other string"
    val concat = aString concat StringUtils.EMPTY concat otherString
    println(concat)
}

/**
 * Extensions functions:
 * - Permiten extender la funcionalidad de clases, incluso cuando no somos los "dueños" de las mismas
 * - Pueden verse como reemplazo de lo que en java conocemos como clases Utils (e.g.: DateUtils)
 * - Hay que usarlos con responsabilidad, y de forma organizada
 * - Las mismas tienen scope y privacidad
 * - (Opinion) No es recomendable extender la funcionalidad de clases primitivas
 * */
fun Int.isEven(): Boolean = this % 2 == 0
fun Int.isOdd(): Boolean = !this.isEven()

/**
 * when: es como un switch con esteroides.
 * - Permite hacer declaraciones del tipo if-else en cascada de forma prolija
 * - Generalmente obliga a cubrir la completitud de los casos (else-branch)
 * - Se pueden hacer matches contra instancias, lo cual se potencia si se utilizan sealed classes (rompe con OOP...)
 * */
fun evenOrOdd(number: Int) = when {
    number.isEven() -> "$number is even"
    number.isOdd() -> "$number is odd"
    else -> throw RuntimeException("a number must be even or odd")
}

fun useExtension() {
    val numbers = (1..10).joinToString { evenOrOdd(it) }
    println(numbers)
}

fun main() {
    handleState(
        SomeClass("immutable", "mutable"),
        SomeDataClass("a", "b")
    )
    useInfix()
    useExtension()
}
