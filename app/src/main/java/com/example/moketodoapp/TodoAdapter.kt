package com.example.moketodoapp

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moketodoapp.databinding.TodoListItemBinding
import com.example.moketodoapp.model.todo.Todo

/**
 * Created by K.Kobayashi on 2023/06/09.
 */
////TODO; ListAdapterには、コンストラクタ引数として、要素比較判定（DiffUtil）のインスタンスを渡す必要がある
//class TodoAdapter : ListAdapter<Todo, TodoAdapter.ViewHolder>(callbacks) {
//
//    // xmlからViewを作るやつ
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        // TODO; layoutInflateの取得
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = TodoListItemBinding.inflate(inflater, parent, false)
//        // TODO;下で作成したViewHolder class にBinding をセットしてかえす
//        return ViewHolder(binding)
//    }
//
//    // 作成されたViewにデータを割り当てるやつ
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        // Adapterが保持している値の取得
//        val todo = getItem(position)
//        // 取得した値をViewHolderにセット
//        holder.bindTo(todo)
//    }
//
//    // TODO:　①　ViewHolder：リスト1行分のViewを保持するクラス
//    class ViewHolder(
//        // todo_list_item.xmlのBindingクラス
//        private val binding: TodoListItemBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun bindTo(todo: Todo?) {
//            if (todo == null) return
//            binding.apply {
//                textTitle.text = todo.title
//                textDetail.text = todo.detail
//                textUpdated.text = DateFormat.format("yyyy/MM/dd hh:mm", todo.modified)
//            }
//        }
//    }
//
//    // TODO:　②　要素（各リストアイテム）比較をするためのコールバックオブジェクトの作成
//    //          ListAdapterは、このコールバックを利用することで、リストの差分を計算するようになる.
//    companion object {
//        private val callbacks = object : DiffUtil.ItemCallback<Todo>() {
//            // 新旧のItemが同じかどうか判別する時に呼ばれる
//            //  判別基準は、モデルに持たせたIDで比較するのが一般的.
//            //  （一意である「id」が異なれば別要素）
//            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
//                return oldItem._id == newItem._id
//            }
//
//            // 新旧の内容が同じかどうかを判定する時に呼ばれる
//            //  （idが同じでも、中身が違えばfalse）
//            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
//                // リストに表示させる内容すべてが一致していればTrueを返す
//                return oldItem.title == newItem.title &&
//                        oldItem.detail == newItem.detail &&
//                        oldItem.modified == newItem.modified
//            }
//
//        }
//    }
//}

class TodoAdapter(
    private val clickListener: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.ViewHolder>(callbacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TodoListItemBinding.inflate(inflater, parent, false)
        // TODO: ★ここでは、以下のpositon の取得はできない。まだViewが生成されてないからか（-1になる）？
        //  TODO;★bindingAdapterPosition　は、　build.gradleに最新のRecyclerVIewを入れないとない（古いRecyclerViewを使用されてしまう）
//        val position = ViewHolder(binding).bindingAdapterPosition

        return ViewHolder(binding)
    }

    /**
     *  現在のリストが更新されたときに呼び出されます。
            submitList(List)にNULLのListが渡された場合、
            もしくはListが送信されていない場合、
        現在のListは空のListとして表現されます。
     */
    override fun onCurrentListChanged(
        previousList: MutableList<Todo>,
        currentList: MutableList<Todo>
    ) {
        super.onCurrentListChanged(previousList, currentList)
    }

    /**
     * Viewにデータを割り当て、リスト項目の作成
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: ★ここで、各リスト共通のアクションを設定する
        holder.itemView.setOnClickListener {
            // リスナーのセットはAdapterの役割、何をするかはAdapterが決めることではない
            println("@@@ positon= $position")
            clickListener(getItem(position))
        }
        val todo = getItem(position)
        holder.bindTo(todo)
    }

    /**
     * リスト1行分のViewを管理
     */
    class ViewHolder(
        private val binding: TodoListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(todo: Todo) {
            binding.apply {
                textTitle.text = todo.title
                textDetail.text = todo.detail
                textUpdated.text = DateFormat.format("yyyy/MM/dd HH:mm", todo.modified)
            }
        }
    }

    companion object {
        private val callbacks = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.detail == newItem.detail &&
                        oldItem.modified == newItem.modified
            }
        }
    }
}

