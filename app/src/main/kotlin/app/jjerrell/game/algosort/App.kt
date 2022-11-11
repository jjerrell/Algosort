package app.jjerrell.game.algosort

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.jjerrell.game.algosort.base.AlgosortViewModel
import app.jjerrell.game.algosort.base.ViewModelState
import app.jjerrell.game.algosort.feature.home.ui.HomePage
import app.jjerrell.game.algosort.features.HomeAndroidFeature
import app.jjerrell.game.algosort.ui.theme.AlgosortTheme
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @source Algosort
 *
 * @author jay
 * @since 10/31/22
 */
@Composable
@OptIn(ExperimentalMaterialNavigationApi::class)
fun App(
    viewModel: AppViewModel = viewModel()
) {
    val bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator()
    val navController: NavHostController = rememberNavController(bottomSheetNavigator)
    AlgosortTheme {
        ModalBottomSheetLayout(bottomSheetNavigator) {
            NavHost(navController, startDestination = viewModel.startRoute) {
                viewModel.registerFeatureHomeGraph(navController, this)
            }
        }
    }
}

@HiltViewModel
class AppViewModel @Inject constructor(
    val homeAndroidFeature: HomeAndroidFeature
) : AlgosortViewModel() {
    val startRoute by lazy { homeAndroidFeature.featureHome.route }

    fun registerFeatureHomeGraph(
        controller: NavHostController,
        builder: NavGraphBuilder
    ) {
        println(controller.toString())
        builder.apply {
            composable(homeAndroidFeature.featureHome.route) {
                HomePage()
            }
        }
    }
}