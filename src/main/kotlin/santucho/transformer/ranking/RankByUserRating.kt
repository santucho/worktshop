package santucho.transformer.ranking

import com.google.inject.Inject
import com.google.inject.Singleton
import org.slf4j.LoggerFactory
import santucho.client.MeliClient
import santucho.client.dto.meli.UserDTO
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
        /**
         * We retrieve all publications' users
         * We'll parallelize this later
         * */
        val users = publications.map { it.seller }.distinct().map {
            /**
             * * If we cannot retrieve a user's info, we ignore it
             * */
            try {
                meliClient.getUser(it.id)
            } catch (ex: Throwable) {
                logger.error("Error retrieving user info")
                null
            }
        }.fold(listOf<UserDTO>()) { acc, elem -> elem?.let { acc + elem } ?: acc }

        /**
         * We try to join user and publication that we already have.
         * Filtering publications that doesn't have user
         * */
        val publicationsWithUser = publications.map { p ->
            joinWithUser(p, users)
        }.fold(listOf<Publication>()) { acc, elem -> elem?.let { acc + elem } ?: acc }

        return publicationsWithUser.sortedByDescending { p -> (p.seller as FullUser).points }
    }

    private fun joinWithUser(
        publication: Publication,
        users: List<UserDTO>
    ): Publication? {
        return users
            .find { it.id == publication.seller.id }?.let {
                publication.copy(seller = transformUser(it))
            }
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
