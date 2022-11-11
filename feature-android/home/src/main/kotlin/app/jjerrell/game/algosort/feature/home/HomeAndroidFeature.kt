package app.jjerrell.game.algosort.feature.home

import app.jjerrell.game.algosort.base.feature.AndroidFeatureApi
import app.jjerrell.game.algosort.base.feature.FeatureNavigationApi

interface IHomeAndroidFeature : AndroidFeatureApi<HomeAndroidNavigation> {
    val config: IHomeConfiguration
}

interface IHomeConfiguration {
    val provideFoo: () -> String
}

sealed interface HomeAndroidNavigation : FeatureNavigationApi {
    class Home(override val route: String) : HomeAndroidNavigation

}

//class HomeAndroidFeature @Inject constructor() : AndroidFeatureApi {
//    override val featureHome = HomeAndroidNavigation.Home
//    override val featureName: String = "HomeAndroidFeature"
//    override fun registerGraph(
//        modifier: Modifier,
//        navGraphBuilder: NavGraphBuilder,
//        navController: NavHostController
//    ) {
//        navGraphBuilder.apply {
//            composable(featureHome.route) {
//                HomePage()
//            }
//        }
//    }
//}