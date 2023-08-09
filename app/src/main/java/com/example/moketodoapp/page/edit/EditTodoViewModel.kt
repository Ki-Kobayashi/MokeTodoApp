package com.example.moketodoapp.page.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moketodoapp.model.todo.Todo
import com.example.moketodoapp.model.todo.TodoDao
import com.example.moketodoapp.repository.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by K.Kobayashi on 2023/06/05.
 *  ★ViewModel: 状態チェック、状態の保持・与えることだけやる
 *  ★Repository：DB / Api アクセスに必要なReqデータを成形する
 */
@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val todoRepo: TodoRepository
): ViewModel() {
    val errMessage = MutableLiveData<String>()
    val successData = MutableLiveData<Todo>()

    fun updateTodo(todo: Todo, title: String, detail: String) {
        if (title.trim().isEmpty() || detail.trim().isEmpty()) {
            errMessage.value = "タイトルと詳細の両方を入力してください。"
            return
        }
        viewModelScope.launch {
            try {
                val updatedTodo = todoRepo.updateTodo(todo, title, detail)
                successData.value = updatedTodo
            } catch (e: Exception) {
                errMessage.value = e.message
            }
        }
    }
}
