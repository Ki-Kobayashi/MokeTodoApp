package com.example.moketodoapp.module

import android.content.Context
import androidx.room.Room
import com.example.moketodoapp.model.todo.TodoDao
import com.example.moketodoapp.model.todo.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by K.Kobayashi on 2023/06/06.
 *
 * RoomのDB操作に必要な、以下のインスタンスを返すモジュールの作成
 *  ★Dao
 *  ★Database
 */

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    /**
     * データベースを生成し注入準備
     *  ※Hiltは、Roomの生成方法を知らない
     *          →　以下のように明示的に知らせる必要がある
     *          @Singleton: 複数個所からインスタンスが必要になっても、一つの同じインスタンスをDIするようになる
     */
    @Singleton
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext ctx: Context
    ) : TodoDatabase{
        return Room.databaseBuilder(
            ctx,
            TodoDatabase::class.java,
            "todo.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoDatabase):TodoDao {
        return db.todoDao()
    }
}
