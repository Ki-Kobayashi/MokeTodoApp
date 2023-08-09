package com.example.moketodoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

//TODO:　
//TODO:　
//TODO:　
//TODO:　
//TODO:　
//TODO:　

// ★TODOAppの開発手順
//      1. NavigationGraphの作成
//      2. 各Fragment（main/ create / detail / edit）の作成（大枠）　
//      3. Modelパッケージ、Todoo / TodoDao / Database作成
//      4. repository作成
//      5. ViewModelを各画面に作成（大枠）
//      6. Hiltモジュール追加（app/top buildgradle）
//      7. Applicationクラスの作成＋マニフェスト追加
//      8. DIする予定のクラス（Activity/Fragmentなど）に@AndroidEntryPoint付与
//      9. RoomModule, TodoRepositoryModule.ktを作成し、HiltでDiするためのModule作成方法を指示
//              →Module指示するのは、
//                     ー・ 「IF系（Repository）」
//                     ー・「ライブラリ依存のもの：（Retrofit / Moshi / Roomなど）」
//      10. viewBindingを使えるようにする（findViewByIdが楽になるやつ）
//          app:build.gradle
//      11. 各FagmentにBindingの追加
//              <書き方は2つ>
//                  MainFragment.
//                  その他Fragment参照.
//      12. fragment_create_todo.xml の見た目を整える
//              ・title、Detailを入力
//              ・Appbarにmenuを表示させ、Todoを保存できるようにする
//              ・保存ボタンにdoneアイコン、複数ボタンがある場合は、三点リーダーでメニュー表示するよう設定する（ifRoom）
//              ・CreateTodoFragmentにもメニューの設定し、保存処理を追加する
//      13. ⑫のAppbarで表示させたいメニューを作成する
//              menu_create
//              場所があるときはIcon表示、ない時は三点リーダー
//              Todoを保存するボタンとする
//              保存処理を追加し、その中でViewModelの処理（todoSave(title, detail)）を.呼ぶ
//      14. ViewModelにtodoSaveメソッドの追加
//              Title, detail のいずれかが、未入力だった場合に、エラーメッセージを格納する。
//      15. CreateTodoFragment：⑭のerrMessageを監視し、値が格納されていればスナックバーの表示
//              画面回転時にスナックバーが毎度表示されないようにすること。
//      16. ViewModel：todoSaveメソッドを完成させる
//              ・repositoryをHiltDiで受け取り、必要な処理を追加
//                      ・RoomにTODOを追
//                      ・処理が完了したら、完了フラグをTrueにし、登録画面から一覧画面へ自動遷移（前ページに戻る）させる.
//              ・Room操作は、非同期であることに注意
//                      ーDB操作、ストレージ読み込み、API通信などでは、処理エラーが発生することもある
//                              ★ エラーハンドリングも実装すること
//                               ★エラー時は、エラーメッセージをスナックバーに表示すること。
//      17. DatabaseInspectorを確認し、値が入っているか確認
//      18.  登録済みTODOを一覧画面に表示させるための recyclerView を作成
//                 ー【新規】 ：リスト位置行分のxml作成
//                 ー【新規】：Adapterの作成（使い勝手の良いListAdapterを使用：差分計算をしてくれる）
//                 ー【Fragment】：リスト表示させたいFragmentに、RecyclerViewを配置
//                 ―【Fragment】：RecyclerViewに、「Adapter」「LayoutManager」をセット
//                 ―【Fragment】：データを読み込みAdapterをセット
//                 ー18にセットするTODOリストをRoomから取得するDao/repo追加
//                 ーTodo一覧画面のVIewModel（MainViewModel）で、Todoの全件取得しておく
//                 ー 取得したTodoリストをAdapterにセット（Todoリストが更新されたら、反映されるようにしておく）
//                 ー動作確認し、ちゃんと表示されるか確認.
//      19.詳細画面の作成
//              ー レイアウト作成
//              ー データクラスをParcelize化させる（一覧から詳細画面に行く際に、該当のTODOデータを受け渡しし表示させたいため）
//              ー SageArgsの導入　+　NavigationGraphで設定しデータを渡せる状態にしておく
//              ー　詳細画面でリスト一覧画面から受け取る予定のtodoオブジェクトを、レイアウトにセット.
//      20.RecyclerVIewの項目が押下された時に、押下されたTODOオブジェクトを持って詳細画面に遷移するようにする
//              ーAdapter内でリスト項目がクリックされたときの設定をする：
//                      onClickLinstener
//                      Adapter自身では、クリック時の処理内容は指定させないのが自然
//                          （リスナーとして、外からもらうのが自然
//                       ★動作確認し、一覧→該当の詳細画面に飛ぶか確認.
//      21. 詳細画面から編集画面へ遷移する処理追加
//              ー　詳細画面：AppBarにメニュー追加（編集、editIcon, ifRoom）
//              ー　詳細Fragment：メニュー処理追加
//              ー　詳細画面から編集画面に遷移にあたり、編集画面に表示させるTodo情報を渡す
//                          NavGraoh +　遷移処理にactionをもたせる.
//      22.　編集画面のレイアウト作成（Todo登録画面と同じ）
//              詳細画面から受け取った、TODO情報を表示させる用実装.
//      23. 編集画面のAppBarに編集完了アイコンありのメニュー追加（ここまで出来たら、動作確認）
//      24.　編集画面：完了ボタン押下時処理を実装
//              ー　メニュータップ時の処理
//              ー　【👇手順で更新メソッドを呼ぶ】
//                      ViewModel　→　リポジトリ　→　Daoの更新メソッド実行
//              ー　VMからFragment側に返すLiveDataに、更新結果のセット
//                      ・エラー時：Snackbarにエラーメッセージ（未入力・RoomDBエラー）
//                      ・成功時：更新済のTodo
//              ー　【編集画面】LiveData監視側Fragmentで処理の実行
//                      ・成功時：更新済Todoをもって、詳細画面に戻る
//                              ー setFragmentResult / setFragmentResultListener　を使用してデータを戻す
//                              ー　受け取ったデータでViewを更新する。
//              ー　現状、詳細画面をバックグラウンドにすると、フォアグラウンドになった際、前回のTodo情報に戻ってしまうため、その修正
//                      ーSavedStateHandle経由で.Todoデータを管理しておく
//                                  （Fragment側で、初期値と変更が合った際の再描画にも対応しておく）
//      25.削除機能を実装
//              ー　詳細画面のメニューに3点リーダー内に「削除」と表示
//              ー　↑押下後、アラートダイアログ（ダイアログFragmentで実装）が表示され、
//                      OKを押すと、「Todo削除」、「一覧へ戻る」、「スナックバー（1件のTodoを削除しました）と表示」
//                      キャンセル時は何もしない
//               ※AlertDialogでは、何のボタンを押下されたかだけを、呼び出し元に伝える。
//                  呼び出し側は、その値を見て処理を出し分ける。
//      26.
//      27.
//      28.
//      29.
//      30.
//      31.
//      32.
//      33.
//      34.
//      35.
//      36.
//      37.
//      38.
//      39.
//      40.


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
