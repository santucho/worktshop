package santucho.`0`

/**
 * Keywords and operators: https://kotlinlang.org/docs/reference/keyword-reference.html
 * Visibility modifiers: https://kotlinlang.org/docs/reference/visibility-modifiers.html
 * */

class MyFirstClass {
    fun helloWorld(userName: String): String {
        return "$HELLO_USER $userName!!"
    }

    /**
     * Companion Object
     * Es una instancia única de un objeto asociado a una clase (singleton).
     * Es utilizado para definir estado o comportamiento común, que no depende de las instancias de la clase
     * Es accesible directamente para todos las instancias de la clase
     * */
    companion object {
        private const val HELLO_USER = "Hello"
    }
}
