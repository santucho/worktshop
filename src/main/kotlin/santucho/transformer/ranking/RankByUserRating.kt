package santucho.transformer.ranking

import arrow.core.Option
import arrow.core.extensions.list.foldable.find
import com.google.inject.Inject
import com.google.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.UserDTO
import santucho.coroutine.runBlocking
import santucho.model.FullUser
import santucho.model.Publication

@Singleton
class RankByUserRating @Inject constructor(
    private val meliClient: MeliClient
) : RankStrategy {
    override fun strategy() = RankType.BY_USER_RATING

    override fun sort(
        sites: List<String>,
        publications: List<Publication>
    ): List<Publication> {
        val users = getUsers(publications)
        /**
         * We try to join user and publicacion that we already have.
         * Then we use fold function over the Option to see the real value and filter publications that doesn't have user
         * */
        val publicationsWithUser = publications.map { p ->
            joinWithUser(p, users)
        }.fold(listOf<Publication>()) { acc, elem -> elem.fold({ acc }, { acc + it }) }

        return publicationsWithUser.sortedByDescending { p -> (p.seller as FullUser).points }
    }

    private fun getUsers(publications: List<Publication>): List<UserDTO> {
        /**
         * We retrieve all publications' users
         * */
        return runBlocking {
            publications.map { it.seller }.distinct().map {
                async { meliClient.getUser(it.id) }
            }.awaitAll().fold(listOf<UserDTO>()) { acc, elem -> elem.fold({ acc }, { acc + it }) }
        }
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
