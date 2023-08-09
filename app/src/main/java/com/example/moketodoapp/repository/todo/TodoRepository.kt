package com.example.moketodoapp.repository.todo

import com.example.moketodoapp.model.todo.Todo
import kotlinx.coroutines.flow.Flow

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
interface TodoRepository {
    fun getAllTodo(): Flow<List<Todo>>
    suspend fun createTodo(title: String, detail: String)
    suspend fun updateTodo(todo: Todo, title: String, detail: String): Todo
    suspend fun deleteTodo(todo: Todo)
}
