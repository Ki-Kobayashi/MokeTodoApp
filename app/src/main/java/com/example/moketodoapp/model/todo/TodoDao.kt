package com.example.moketodoapp.model.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// ★Room定義手順
//      ①Dataクラスの作成（@Entity / @PrimaryKey等付与）
//      ②Daoの作成（＠Dao / 各種DB操作アノテーションを付与：基本suspend or Return Flow）
//      ③データベース定義を抽象クラス（RoomDatabase継承）で作成
//          （＠Database付与 / Daoを返す抽象メソッド）


/**
 * Created by K.Kobayashi on 2023/06/04.
 */
// Roomでは、Dao（Dbを操作するためのクラス）は　Interface　で定義する
@Dao
interface TodoDao {
    // 戻り値の型をKotlinコルーチンのFlow<T>にすることで、データベースに変更があると新しい値が流れて来るようになる
    //          戻り値がFlowの時、suspendは不要.
    //          （監視機能が付与される）
    @Query("select * from todo order by modified desc")
    fun getAll(): Flow<List<Todo>>


    @Query("select * from Todo where created < :created order by created desc limit :limit")
    fun getWithCreated(created: Long, limit: Int): Flow<List<Todo>>

    // Roomの操作は、UIスレッドでできないため、suspend関数とする
    //      suspend 関数とは非同期処理のための仕組みであって、「別の suspend 関数を呼び出すために使用する」
    //          https://qiita.com/duke105/items/b5be074c79c6bed4d560
    //      ★以下の処理では、処理速度の遅い【IO】を伴う処理のため、非同期処理にすべき
    //          ・HTTPでの通信
    //          ・ストレージへの保存
    //          ・ストレージからの読み込み
    @Insert
    suspend fun createTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo): Int

    @Delete
    suspend fun deleteTodo(todo: Todo)

}

// TODO: ROOM　更新されない。。repoまではうまくいってそう。。。
// TODO:　一覧更新は、リストデータ（todoList）を監視し、adapter.submitList(it)に再設定する必要あり
