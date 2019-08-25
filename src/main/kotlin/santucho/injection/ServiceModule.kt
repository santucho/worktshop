package santucho.injection

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import santucho.transformer.ranking.RankByAmount
import santucho.transformer.ranking.RankByDiscount
import santucho.transformer.ranking.RankByUserRating
import santucho.transformer.ranking.RankStrategy

class ServiceModule : AbstractModule() {

    override fun configure() {
        val rankStrategiesBinder = Multibinder.newSetBinder(binder(), RankStrategy::class.java)
        rankStrategiesBinder.addBinding().to(RankByAmount::class.java)
        rankStrategiesBinder.addBinding().to(RankByDiscount::class.java)
        rankStrategiesBinder.addBinding().to(RankByUserRating::class.java)
    }
}
