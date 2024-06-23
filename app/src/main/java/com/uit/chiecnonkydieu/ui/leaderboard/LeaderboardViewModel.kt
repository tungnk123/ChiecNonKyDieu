import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.model.LeaderboardItem
import com.uit.chiecnonkydieu.model.toLeaderboardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {
    val appContainer = AppContainer()

    private val _playersList = MutableLiveData<List<LeaderboardItem>>()
    val playersList: LiveData<List<LeaderboardItem>> get() = _playersList

    fun getAllPlayers() {
        viewModelScope.launch(Dispatchers.IO) {
            val players = appContainer.gemApi.getAllPlayers().mapIndexed { index, playerDto ->
                playerDto.toLeaderboardItem(rank = index + 1)
            }
            _playersList.postValue(players)
        }
    }
}
