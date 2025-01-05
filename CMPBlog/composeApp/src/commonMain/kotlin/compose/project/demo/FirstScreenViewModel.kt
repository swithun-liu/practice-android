package compose.project.demo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FirstScreenViewModel: ViewModel() {
    private val innerUIState = MutableStateFlow(FirstScreenUIState())
    val uiState = innerUIState.asStateFlow()

    fun changeIsShowContent() {
        innerUIState.update {
            it.copy(showContent = !it.showContent)
        }
    }

    fun changeLocation(location: String) {
        innerUIState.update {
            it.copy(location = location)
        }
    }
}