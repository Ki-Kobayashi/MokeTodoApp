package com.example.moketodoapp.page.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moketodoapp.model.todo.Todo
import com.example.moketodoapp.repository.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    // APIやローカルのDBなどから取得できるリソース：データが大きいため、Activity再生成後に”再取得”
    // ユーザー入力値など、そこにしかない値：SavedStateHandle / onSaveInstanceState に保存
    state: SavedStateHandle,
    private val repository: TodoRepository
): ViewModel() {

    val todo = state.getLiveData<Todo>(KEY_STATE_TODO)
    // TODO:以下読み取りだけの時は必要（↑は private にする）。
    //      FragmentでLiveDataを初期化したら、新たに値をセットする必要がある時は不要
//    val todo: LiveData<Todo> get() = _todo

    val errMessage = MutableLiveData("")
    val isSuccessDelete = MutableLiveData(false)

    fun deleteTodo() {
        viewModelScope.launch {
            try {
                val todo = this@DetailTodoViewModel.todo.value ?: return@launch
                repository.deleteTodo(todo)
                isSuccessDelete.value = true
            } catch (e: Exception) {
                errMessage.value = e.message
            }
        }
    }

    companion object {
        private const val KEY_STATE_TODO = "todo_key"
    }
}
