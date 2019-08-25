package santucho.transformer.ranking

import arrow.core.Option
import arrow.core.extensions.list.foldable.find
import arrow.core.toOption
import com.google.inject.Singleton
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.UserDTO
import santucho.model.FullUser
import santucho.model.Publication

@Singleton
class RankByUserRating : RankStrategy {
    override fun strategy() = RankType.BY_USER_RATING

    override fun sort(
        meliClient: MeliClient,
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication> {
        /**
         * We retrieve all publicacions' users
         * //ToDo esto es re paralelizable, de hecho es el cuello de botella
         * */
        val users = publications.map { it.seller }.distinct().map {
            meliClient.getUser(it.id)
        }.fold(listOf<UserDTO>()) { acc, elem -> elem.fold({ acc }, { acc + it }) }

        /**
         * We try to join user and publicacion that we already have.
         * Then we use fold function over the Option to see the real value and filter publications that doesn't have user
         * */
        val publicationsWithUser = publications.map { p ->
            joinWithUser(p, users)
        }.fold(listOf<Publication>()) { acc, elem -> elem.fold({ acc }, { acc + it }) }

        return publicationsWithUser.sortedByDescending { p -> (p.seller as FullUser).points }
    }

    private fun joinWithUser(
        publication: Publication,
        users: List<UserDTO>
    ): Option<Publication> {
        return users
            .find { it.id == publication.seller.id }.map { transformUser(it) }
            .map { seller -> Publication.publicationSeller.set(publication, seller) }
    }

    private fun transformUser(user: UserDTO): FullUser {
        return FullUser(
            user.id,
            user.siteId,
            user.nickname,
            user.countryId,
            user.permalink,
            user.points
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RankByUserRating::class.java)
    }
}