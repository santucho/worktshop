package santucho.`0`

/**
 * Clases
 * - class: Clase como la que conocemos de Java
 * - data class: generalmente utilizadas para representar DTOs y Model. Simil case class de Scala.
 * - sealed class: utilizadas para representar jerarquías restringidas
 *
 * Las clases por default son concretas/instanciables, pero pueden ser declaradas como abstractas,
 * en este sentido se comportan igual que en Java.
 *
 * Las clases por default (a diferencia de Java) son final.
 * Por lo cual, si queremos que se pueda extender una clase, la misma debe declararse como `open class
 * */

/**
 * https://kotlinlang.org/docs/tutorials/kotlin-for-py/classes.html
 * La declaración de los atributos dentro de los () genera un constructor primario que requiere los mismos
 */
class SomeClass(
    val someImmutableAttribute: String,
    var mutableAttribute: String,
    var mutableAndNullableAttribute: String?
) {
    /**
     * La definición de constructores secundarios requieren de una llamada al constructor primario
     */
    constructor(
        someImmutableAttribute: String,
        mutableAttribute: String
    ) : this(someImmutableAttribute, mutableAttribute, null)
}

/**
 * https://kotlinlang.org/docs/reference/data-classes.html
 * Las data class son clases que ya vienen provistas de:
 * - equals() / hashCode()
 * - toString()
 * - copy()
 * - permiten declaraciones de variables desestructuradas
 *
 * Requieren que:
 * - al menos un constructor primario con un atributo val/var
 * - todos los parámetros del constructor primario deben ser val/var
 * */
data class SomeDataClass(
    val a: String,
    val b: String
)

/**
 * https://kotlinlang.org/docs/reference/sealed-classes.html
 * */
sealed class HttpError {
    object BadRequest : HttpError()
    object NotFound : HttpError()
    object InternalServerError : HttpError()
    data class UnknownError(
        val description: String
    ) : HttpError()
}
