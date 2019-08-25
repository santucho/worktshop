package santucho.`2`

/**
 * En Kotlin, la construcción y el manejo de mapas resulta bastante más sencilla que en Java
 * - Utiliza el concepto de Pair, equivalente al Entry que usamos en Java
 *   Pair es una dataclass, que entiende .first (key) y .second (value)
 *
 *   val pairExample = Pair("key", "value")
 *   val otherPairExample = "otherKey" to "otherValue"
 * */
fun main() {
    val wpt = WorldPadelTour()
    val players = wpt.getPlayers()

    val singlesRanking = players.sortedByDescending { it.points }.mapIndexed { index, p ->
        index + 1 to p
    }.toMap()

    val top5 = singlesRanking.filterKeys { it <= 5 }

    val doublesRanking = wpt.getTeams().sortedByDescending { team ->
        team.first.points + team.second.points
    }.mapIndexed { index, team ->
        index + 1 to team
    }.toMap()

    top5.forEach { (rank, player) ->
        println("Player #$rank - $player")
    }

    doublesRanking.forEach { (rank, team) ->
        println("Team #$rank - ${team.first} & ${team.second}")
    }
}

class WorldPadelTour {

    fun getPlayers() =
        listOf(paquito, lebron, galan, lima, sanyo, maxi, bela, stupa, tapia, mati).shuffled()

    fun getTeams() =
        listOf(
            paquito to lima,
            galan to lebron,
            sanyo to stupa,
            mati to maxi,
            bela to tapia
        )

    companion object {
        val paquito = PadelPlayer("Francisco", "Paquito", "Navarro", 12010)
        val lebron = PadelPlayer("Juan", "El Lobo", "Lebron", 11330)
        /**
         * Ale Galán no tiene apodo, salteamos ese parámetro usando named parameters y aprovechando que tiene un default definido
         * */
        val galan = PadelPlayer("Alejandro", surname = "Galan", points = 11100)
        val lima = PadelPlayer("Pablo", "El Cañón de Porto Alegre", "Lima", 10860)
        val sanyo = PadelPlayer("Carlos Daniel", "Sanyo", "Gutierrez", 9020)
        val maxi = PadelPlayer("Maximiliano", "El Tiburón", "Sánchez", 9020)
        val bela = PadelPlayer("Fernando", "El Jefe", "Belasterguín", 7615)
        val stupa = PadelPlayer("Franco", "Stupa", "Stupaczuk", 5850)
        val tapia = PadelPlayer("Agustín", "El Mozart de Catamarca", "Tapia", 5770)
        val mati = PadelPlayer("Matías", "El Warrior", "Díaz Sangiorgio", 5645)
    }
}

data class PadelPlayer(
    val name: String,
    val nick: String? = null,
    val surname: String,
    val points: Int
) {
    override fun toString(): String {
        val nick = this.nick?.let { " '$it'" } ?: ""
        return "${this.name}$nick ${this.surname}"
    }
}
