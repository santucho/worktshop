Worktshop
===

### Kotlin + Arrow

Breve workshop sobre cómo ser más felices evitando side-effects, mutabilidad y la lluvia de excepciones…

![kotlin](img/kotlin.svg)
![arrow](img/arrow.png)
---

Sobre Kotlin:
- Kotlin es un lenguaje de programación creado por JetBrains en 2012. Que presenta como característica ser 100% interoperable con Java
- Lenguaje oficial Android
- Presenta múltiples syntactic sugars que posibilitan un desarrollo más agil
- Posee un set de funciones para manejo de listas más completo e intuitivo que el de Java

Sobre Arrow-Kt:
- Es una *biblioteca* para programación funcional tipada en Kotlin
- Nos provee de DataTypes (Option, Either, Try, Validated, entre otros)
- Nos provee de TypeClasses, Effects (para el manejo de side-effects), y Optics (abstracciones para actualizar de manera funcional y elegante objetos inmutables)

Qué vas a encontrar en este repo?
- Esqueleto de webApp en Kotlin usando [Spark](https://github.com/perwendel/spark) + [Guice](https://github.com/google/guice) + [Arrow](https://github.com/arrow-kt/arrow)
- Distintos branches que iteran la resolución de una misma problemática desde un enfoque más objetoso hacia un enfoque más funcional

### Índice

- [kt-basis](https://github.com/santucho/worktshop/tree/kt-basis)
- [new-project](https://github.com/santucho/worktshop/tree/new-project)
- [template-app](https://github.com/santucho/worktshop/tree/template-app)
- [rest-api](https://github.com/santucho/worktshop/tree/rest-api)
- [parallelism](https://github.com/santucho/worktshop/tree/parallelism)
- [arrow](https://github.com/santucho/worktshop/tree/arrow)

### Propuesta

Utilizar la API de MercadoLibre para consultar publicaciones de algún producto para distintos sitios y elaborar múltiples rankings de los resultados según distintos criterios:
- Por monto de menor a mayor (tener en cuenta que cada sitio devuelve sus publicaciones en moneda local, con lo cual es necesario unificar la moneda)
- Por % de descuento (ver campo 'original_price')
- Por el rating del usuario que está publicando (ver información del usuario)

Posteriormente aprovechar la inmutabilidad del modelo que hemos creado para procesar los rankings en paralelo
