package com.example.moketodoapp.module

import com.example.moketodoapp.repository.todo.TodoRepository
import com.example.moketodoapp.repository.todo.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by K.Kobayashi on 2023/06/06.
 */
// ToDoRepositoryインターフェースなインスタンスを要求されたとき、
//  何を作って返せばいいかもHiltに教える必要がある

//Hiltは
// 「ToDoRepositoryなインスタンスを要求されたら、ToDoRepositoryImplを渡せばよい」
// 　・引数：impl
//   ・戻り値：IF
// と判断してくれる
@Module
@InstallIn(SingletonComponent::class)
abstract class TodoRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTodoRepository(
        impl: TodoRepositoryImpl
    ): TodoRepository
}

