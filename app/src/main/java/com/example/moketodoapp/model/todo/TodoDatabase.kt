package com.example.moketodoapp.model.todo

import androidx.room.Database
import androidx.room.RoomDatabase


// ★Room定義手順
//      ①Dataクラスの作成（@Entity / @PrimaryKey等付与）
//      ②Daoの作成（＠Dao / 各種DB操作アノテーションを付与：基本suspend or Return Flow）
//      ③データベース定義を抽象クラス（RoomDatabase継承）で作成
//          （＠Database付与 / Daoを返す抽象メソッド）

/**
 * Created by K.Kobayashi on 2023/06/04.
 */
// ★複数のテーブルがある際は、entitiesの配列にモデルを追加していく
// TODO:（exportSchema = falseがないとエラー図れる）
@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase: RoomDatabase() {
    // ★複数のテーブルがある際は、以下のような抽象メソッドを追加する
    abstract fun todoDao(): TodoDao
}
