package com.example.moketodoapp.model.todo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// ★Room定義手順
//      ①Dataクラスの作成（@Entity / @PrimaryKey等付与）
//      ②Daoの作成（＠Dao / 各種DB操作アノテーションを付与：基本suspend or Return Flow）
//      ③データベース定義を抽象クラス（RoomDatabase継承）で作成
//          （＠Database付与 / Daoを返す抽象メソッド）


/**
 * Created by K.Kobayashi on 2023/06/04.
 */
@Entity
@Parcelize
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val _id: Int = 0,
    val title: String,
    val detail: String,
    val created: Long,
    val modified: Long
): Parcelable
