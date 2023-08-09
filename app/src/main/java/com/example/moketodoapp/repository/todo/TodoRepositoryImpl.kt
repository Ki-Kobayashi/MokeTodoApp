package com.example.moketodoapp.repository.todo

import com.example.moketodoapp.model.todo.Todo
import com.example.moketodoapp.model.todo.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
// リポジトリの存在理由：
//      今は内部DBだけに保存しているが、今後サーバー保存したくなったときは、個々だけ書き換えればいい。
//      呼び出し側で処理を変更する必要がなく、メンテナンス性が上がる。
class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {
    override fun getAllTodo(): Flow<List<Todo>> {
        return dao.getAll()
    }

    override suspend fun createTodo(title: String, detail: String) {
        val now = System.currentTimeMillis()
        val todo = Todo(title = title, detail = detail, created = now, modified = now)
        withContext(Dispatchers.IO) {
            dao.createTodo(todo)
        }
    }

    override suspend fun updateTodo(todo: Todo, title: String, detail: String): Todo {
        val now = System.currentTimeMillis()
        val  updatedTodo = todo.copy(title = title, detail = detail, modified = now)

        println("@@@@@ Repo: $updatedTodo")
        //     TODO: ★  RoomアクセスはIOスレッドでないと例外を投げて落ちる
        withContext(Dispatchers.IO) {
            val count = dao.updateTodo(updatedTodo)
            println("@@@@@ daoRes: $count")
        }
        return updatedTodo
    }

    override suspend fun deleteTodo(todo: Todo) {
        withContext(Dispatchers.IO) {
            dao.deleteTodo(todo)
        }
    }
}
