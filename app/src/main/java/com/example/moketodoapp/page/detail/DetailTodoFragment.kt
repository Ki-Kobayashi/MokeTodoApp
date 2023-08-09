package com.example.moketodoapp.page.detail

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moketodoapp.R
import com.example.moketodoapp.common.ui.ConfirmDialogFragment
import com.example.moketodoapp.databinding.FragmentDetailTodoBinding
import com.example.moketodoapp.model.todo.Todo
import com.example.moketodoapp.page.edit.EditTodoFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by K.Kobayashi on 2023/06/04.
 *
 * // ★TODO:【従来】 ViewBinding、継承Fragmentのコンストラクタを使用しないFragment 生成の書き方
 */
@AndroidEntryPoint
class DetailTodoFragment : Fragment(R.layout.fragment_detail_todo) {
    private val vm: DetailTodoViewModel by viewModels()

    private var _binding: FragmentDetailTodoBinding? = null
    private val binding: FragmentDetailTodoBinding get() = _binding!!

    private val args: DetailTodoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: ここで別Fragmentからの返答をまつリスナーセット
        setResultListener()
        // TODO:savedInstanceStateがnull → 初回表示（再構築後でない）
        if (savedInstanceState == null) {
            // TODO★★★：初回表示 ＝ 一覧画面から遷移 ＝ 一覧画面から受け取った値をセット
            vm.todo.value = args.todo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        requireActivity().addMenuProvider(
            crateMenu(),
            // 以下2引数：NavigationComponent利用時は必須（メニューが2重表示されたり、不要箇所での表示を防ぐ目的）
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    private fun setResultListener() {
        // TODO:遷移先から値がセットされたら、呼び出される
        setFragmentResultListener(EditTodoFragment.REQUEST_KEY) { _, bundle ->
            // セットされる値は、BundleにラップされたParcerableとなっている
            val todo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(EditTodoFragment.BUNDLE_KEY, Todo::class.java)
            } else {
                // APIレベル33から、以下は非推奨（引数が違うだけ）
                bundle.getParcelable(EditTodoFragment.BUNDLE_KEY) as? Todo
            }
            if (todo == null) return@setFragmentResultListener
            // ★ TODO★★★: 更新済の返答データ（TodoData）を、ViewModelのLiveDataに格納
            vm.todo.value = todo
        }

        setFragmentResultListener(ConfirmDialogFragment.REQUEST_KEY) { _, bundle ->
            when (bundle.getInt(ConfirmDialogFragment.BUNDLE_KEY)) {
                DialogInterface.BUTTON_POSITIVE -> vm.deleteTodo()
//                DialogInterface.BUTTON_NEGATIVE -> {
//
//                }
            }
        }
    }

    private fun crateMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_edit -> return menuEditTodo()
                    R.id.action_delete -> return menuDeleteTodo()
                }
                return true
            }

        }
    }

    private fun initViews(view: View) {
        this._binding = FragmentDetailTodoBinding.bind(view)
        val todo = args.todo
        binding.apply {
            textDetailTitle.text = todo.title
            textDetailContent.text = todo.detail
        }
        setObserve()
    }

    /**
     * ViewModelのLiveDataの監視
     */
    private fun setObserve() {
        // TODO★★★：ViewModelのLiveData【Todo】の内容が変わったら、Viewを再描画する
        vm.apply {
            todo.observe(viewLifecycleOwner) { todo ->
                binding.apply {
                    textDetailTitle.text = todo.title
                    textDetailContent.text = todo.detail
                }
            }

            isSuccessDelete.observe(viewLifecycleOwner) {
                if (!it) return@observe
                showSnackbar(getString(R.string.snack_deleted))
                // TODO:第１引数：どこの画面に戻りたいか、第2引数：戻りたい画面も閉じるか
                findNavController().popBackStack(R.id.mainFragment, false)
                isSuccessDelete.value = false
            }

            errMessage.observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe
                showSnackbar(it)
                errMessage.value = ""
            }
        }
    }

    private fun menuEditTodo(): Boolean {
        //  TODO★★★: メニュー押下で編集画面に遷移 ＞ 最新のTodoデータ（viewModelのLiveData）を渡す
        //      格納されてない（null）なら、そのままreturn
        val todo = vm.todo.value ?: return true
        val action =
            DetailTodoFragmentDirections.actionDetailTodoFragmentToEditTodoFragment(
                todo
            )
        findNavController().navigate(action)
        return true
    }

    private fun menuDeleteTodo(): Boolean {
        val action =
            DetailTodoFragmentDirections.actionDetailTodoFragmentToConfirmDialogFragment(
                getString(R.string.alert_dialog_delete)
            )
        findNavController().navigate(action)
        return true
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
    }
}
