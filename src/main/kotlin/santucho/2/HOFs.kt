package santucho.`2`

/**
 * @see https://kotlinlang.org/docs/reference/lambdas.html
 *
 * Higher Order Functions & Lambdas:
 *
 * Kotlin functions are first-class,
 * which means that they can be stored in variables and data structures,
 * passed as arguments to and returned from other higher-order functions.
 * You can operate with functions in any way that is possible for other non-function values.
 *
 * To facilitate this, Kotlin, as a statically typed programming language,
 * uses a family of function types to represent functions and provides a set of specialized language constructs,
 * such as lambda expressions.
 *
 *
 * Higher-Order Functions:
 *
 * A higher-order function is a function that takes functions as parameters, or returns a function.
 * */

fun hof(aNumber: Int, otherFun: (Int) -> String): String {
    return otherFun(aNumber)
}

fun main() {
    val aHundredTimes = { aNumber: Int ->  "${aNumber * 100}" }
    hof(1, aHundredTimes)
    hof(1) { "${it * 100}" }
}

