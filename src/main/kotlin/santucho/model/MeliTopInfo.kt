package santucho.model

import arrow.core.Nel

data class MeliTopInfo(
    val product: String,
    val sites: Nel<String>
)