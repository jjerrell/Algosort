package app.jjerrell.game.algosort.base.feature

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * @source Algosort
 *
 * @author jay
 * @since 10/29/22
 */
interface AndroidFeatureApi<T: FeatureNavigationApi> : FeatureApi {
    override val appLayer: ArchitectureLayer
        get() = ArchitectureLayer.Platform
    val navigation: T
    fun registerGraph(
        modifier: Modifier = Modifier,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
}

interface FeatureNavigationApi {
    val route: String
}