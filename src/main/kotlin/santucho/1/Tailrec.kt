package santucho.`1`

/**
 * tailrec: es una forma de recursividad donde "nos comprometemos" a limitar los efectos de la recursividad a la
 * última linea de código a ejecutar, de forma tal que no se acumulen en el stack operaciones sin resolver.
 * Las mismas se anotan como `tailrec` y le permiten al compilador convertir el código en un for/while.
 * */

fun fib(number: Int): Int {
    tailrec fun resolveFib(index: Int = 0, f0: Int = 0, f1: Int = 1): Int {
        return when (number) {
            0 -> f0
            1 -> f1
            else -> when(index) {
                number - 1  -> f0 + f1
                else -> resolveFib(index + 1, f1, f0 + f1)
            }
        }
    }
    return resolveFib()
}


fun main() {
    println("Fib 0: ${fib(0)}")
    println("Fib 1: ${fib(1)}")
    println("Fib 2: ${fib(2)}")
    println("Fib 3: ${fib(3)}")
    println("Fib 4: ${fib(4)}")
    println("Fib 5: ${fib(5)}")
    println("Fib 6: ${fib(6)}")
    println("Fib 7: ${fib(7)}")
}