package com.example.moketodoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by K.Kobayashi on 2023/06/05.
 */
// アプリケーションクラスを作成し、以下のアノテーションを付与しないと、Hilt使用できない
    //      Applicationクラスは、manifestファイルに登録する必要あり
@HiltAndroidApp
class TodoApplication: Application() {
}
