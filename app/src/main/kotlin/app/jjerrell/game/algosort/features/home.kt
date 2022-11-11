package app.jjerrell.game.algosort.features

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import app.jjerrell.game.algosort.feature.home.HomeAndroidNavigation
import app.jjerrell.game.algosort.feature.home.HomeConfiguration
import app.jjerrell.game.algosort.feature.home.IHomeAndroidFeature
import app.jjerrell.game.algosort.feature.home.IHomeConfiguration
import app.jjerrell.game.algosort.feature.home.ui.HomePage
import javax.inject.Inject

/**
 * @source Algosort
 *
 * @author jay
 * @since 11/4/22
 */
class HomeAndroidFeature @Inject constructor(
    @Inject override val config: IHomeConfiguration
) : IHomeAndroidFeature {
//    override val config = HomeConfiguration(
//        provideFoo = { "Foo" }
//    )
    override val navigation: Class<HomeAndroidNavigation>
        get() = HomeAndroidNavigation::class.java

    //    override val featureHome
//        get() = HomeAndroidNavigation.Home
    override val featureName: String
        get() = "HomeAndroidFeature"

    override fun registerGraph(
        modifier: Modifier,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.apply {
            composable(featureHome.route) {
                HomePage()
            }
        }
    }
}