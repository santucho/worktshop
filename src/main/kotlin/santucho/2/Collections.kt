package santucho.`2`

/**
 * Manejo de colecciones:
 *
 * - Kotlin tiene un manejo de colecciones mucho más completo, intuitivo y práctico que el de Java,
 * empezando por la no necesidad del uso de .stream()
 * - Al no haber uso de .stream(), tampoco es necesario el .collect(Collectors.toList())
 *
 * - Entre las funciones que nos vamos a encontrar se encuentran
 *  -> forEach, forEachIndexed
 *  -> map, mapNotNull, mapIndexed, ...
 *  -> filter, filterNot, filterNotNull, ...
 *  -> flatten
 *  -> flatMap
 *  -> first, firstOrNull, last, lastOrNull
 *  -> find, findLast
 * */

fun main() {
    val chars = ('a'..'z').toList()
    chars.forEach { print(it) }
    val capChars = chars.map { it.toUpperCase() }
    println(capChars)
    val vowels = chars.filter { c -> Collections.VOWELS.contains(c) }
    println(vowels)

    val odds = listOf(1, 3, 5, 7, 9)
    val evens = listOf(2, 4, 6, 8, 10)
    val numbersBF = listOf(odds, evens)
    println(numbersBF)
    val numbers = numbersBF.flatten() // Esto podría hacerse mejor escribiendo odds + evens
    println(numbers)
    val moreNumbers = numbers.flatMap { prevAndNext(it) }
    println(moreNumbers)

    // Wrappers de firstOfNull y lastOfNull
    val sevenByFind = numbers.find { it == 7 } // Find devuelve null si ninguno cumple con la condición
    val sevenByFindLast = numbers.findLast { it == 7 } // FindLast devuelve null si ninguno cumple con la condición
    println("$sevenByFind, $sevenByFindLast")

    val msg = replaceMessageParamsWithFold("template message {0} {1}", listOf("param", "otherParam"))
    println(msg)
}

fun replaceMessageParamsWithFold(message: String, args: List<String>): String {
    return args.foldIndexed(message) { index, acc, arg ->
        acc.replace("{$index}", arg)
    }
}

private fun prevAndNext(number: Int) = listOf(number - 1, number + 1)

object Collections {
    val VOWELS = listOf('a', 'e', 'i', 'o', 'u')
}
