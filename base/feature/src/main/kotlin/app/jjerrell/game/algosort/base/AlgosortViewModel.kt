package app.jjerrell.game.algosort.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @source Algosort
 *
 * @author jay
 * @since 11/1/22
 */
abstract class AlgosortViewModel : ViewModel() {
    fun <T> execute(
        action: suspend () -> T,
        onSuccess: (result: T) -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = action()
                onSuccess(result)
            } catch (error: Throwable) {
                onFailure(error)
            }
        }
    }
}

sealed interface ViewModelState<out T> {
    val result: T?
        get() = null

    // == Generics for overall state
    object Empty : ViewModelState<Nothing>
    sealed class Loading<R>(override val result: R?) : ViewModelState<R> {
        class Initializing<R> : Loading<R>(null)
        data class Refreshing<R>(override val result: R?) : Loading<R>(result)
    }
    sealed class Result<R>(override val result: R?) : ViewModelState<R> {
        data class Success<R>(override val result: R) : Result<R>(result)
        data class Error<R>(val thrown: Throwable?, override val result: R) : Result<R>(result)
        data class ApiError<R>(val thrown: Throwable?, override val result: R? = null) : Result<R>(result)
    }

    companion object {
        inline fun <reified R> loading(
            cache: R? = null,
            forceRefresh: Boolean = false
        ): Loading<R> {
            return if (cache != null || forceRefresh) {
                Loading.Refreshing(result = cache)
            } else {
                Loading.Initializing()
            }
        }

        inline fun <reified R> result(
            value: R?,
            error: Throwable? = null,
            forceApiOnError: Boolean = false
        ): Result<R> {
            return when {
                error != null -> {
                    if (value != null && !forceApiOnError)
                        Result.Error(thrown = error, result = value)
                    else
                        Result.ApiError(thrown = error, result = value)
                }
                value != null -> {
                    Result.Success(result = value)
                }
                else -> Result.ApiError(
                    thrown = Throwable("Unknown client error"),
                    result = null
                )
            }
        }
    }
}
