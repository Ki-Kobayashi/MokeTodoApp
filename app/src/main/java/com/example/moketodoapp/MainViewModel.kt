package com.example.moketodoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.example.moketodoapp.repository.todo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val todoRepo: TodoRepository
): ViewModel() {
    val todoList = todoRepo.getAllTodo().asLiveData()
}
