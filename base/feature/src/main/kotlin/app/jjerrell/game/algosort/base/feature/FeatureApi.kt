package app.jjerrell.game.algosort.base.feature

/**
 * @source Algosort
 *
 * @author jay
 * @since 10/29/22
 */
interface FeatureApi {
    val featureName: String
    val appLayer: ArchitectureLayer
}

enum class ArchitectureLayer {
    Platform,
    Domain,
    Data
}