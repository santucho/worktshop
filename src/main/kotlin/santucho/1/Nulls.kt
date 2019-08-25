package santucho.`1`

import kotlin.random.Random

/**
 * Kotlin permite declarar variables como nulleables agregando `?` a continuación del tipo de dato.
 * Cuando hacemos esto, el compilador nos obliga a checkear antes de usar dichas variables, y a tratarlas de forma segura.
 * */
fun main() {
    val randomOrNull = getRandomOrNull()

//    val randomPlusOne = randomOrNull + 1
    val randomPlusOne = randomOrNull?.plus(1)
    println(randomPlusOne) // Esto puede o bien imprimir null o random + 1

    if (randomOrNull != null) {
        // Kotlin sabe que en este contexto randomOrNull ya no puede ser null y lo trata como tal
        println("Imprimo $randomOrNull solo cuando tiene un valor")
    }

    /**
     * Blocks
     * */
    randomOrNull?.let { random -> // Dentro de este bloque random no es null
        println("Imprimo ${random + 1} solo cuando tiene un valor 2") // Esto sólo imprime random + 1
    }

    /**
     * Elvis Operator
     * */
    val randomOrDefault = randomOrNull ?: 0
    println(randomOrDefault)
}

fun getRandomOrNull(): Int? {
    val rdm = Random.nextInt(0, 100)
    return if (rdm < 50)
        rdm
    else
        null
}
