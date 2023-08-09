package com.example.moketodoapp.page.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moketodoapp.repository.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
// TODO: ViewModelに引数のあるコンストラクタをセットするとアプリがクラッシュしてしまう
//          TODO: →Hiltライブラリを使えば、ViewModelでも引数ありのコンストラクタが使用できるようになる
@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    private val repo: TodoRepository
): ViewModel() {
    val errMessage = MutableLiveData<String>()
    val isDoneCreateTodo = MutableLiveData<Boolean>()

    /**
     * Todo保存処理
     */
    fun todoSave(title: String, detail: String) {
        if (title.trim().isEmpty() || detail.trim().isEmpty()) {
            errMessage.value = "タイトルか詳細が入力されていません。"
            return
        }
        // TODO:ViewModelScopeを使用することで、
        //      ・画面回転時も処理は継続
        //      ・バックボタンなどでViewModelが破棄される際は、処理も自動キャンセル
        //     されるようになる
        //     ・TODO: ★Roomは、ワーカスレッドでDBアクセスする決まり（Android公式より）
//        viewModelScope.launch(Dispatchers.IO) {
        viewModelScope.launch {
            try {
                repo.createTodo(title, detail)
                // TODO; postValueでワーカスレッドからメインスレッドに値をポスト（.value = の場合は、ダイレクトにメインスレッドにセット）
//                isDoneCreateTodo.postValue(true)
                isDoneCreateTodo.value = true
            } catch (e: Exception) {
//                errMessage.postValue(e.message)
                errMessage.value = e.message
            }
        }
    }
}
