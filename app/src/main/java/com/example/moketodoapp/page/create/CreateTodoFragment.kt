package com.example.moketodoapp.page.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.moketodoapp.R
import com.example.moketodoapp.databinding.FragmentCreateTodoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by K.Kobayashi on 2023/06/04.
 *
 * 【ViewBinding】　R.layout.xx を記載しないでViewBindingのみでFragment作成する方法
 *
 */
@AndroidEntryPoint
class CreateTodoFragment : Fragment() {
    private val vm: CreateTodoViewModel by viewModels()

    private var _binding: FragmentCreateTodoBinding? = null

    // TODO: get()がないと、「InstantiationException」が吐き出される
    private val binding: FragmentCreateTodoBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // 　★以下利用の場合は、継承FragmentにR.id.～は不要。それがなくてもinfrateで生成されるため。
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateTodoBinding.inflate(inflater, container, false)
        // ここで、_binding初期化すると、Nullポで落ちる＋メニュー増殖
        requireActivity().addMenuProvider(
            createMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        return binding.root
    }

    // TODO; LiveDataの監視設定は、onViewCreatedで行う
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        this._binding = FragmentCreateTodoBinding.bind(view)

        // errMessageを監視し、中身があればスナックバーの表示
        vm.errMessage.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
            // 以下を記載しないと、画面回転の度に、スナックバーが表示されてしまう
            vm.errMessage.value = ""
        }
        // TODO保存成功なら前画面へ戻る
        vm.isDoneCreateTodo.observe(viewLifecycleOwner) {
            if (it) findNavController().popBackStack()
        }

    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        this._binding = FragmentCreateTodoBinding.bind(view)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }


    /**
     * 新規Todo保存処理
     */
    private fun todoSave() {
        val title = binding.inputTextTitle.text.toString()
        val detail = binding.inputTextDetail.text.toString()

        vm.todoSave(title, detail)
    }

    /**
     * Appbar表示メニューの生成
     */
    private fun createMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_create, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_save -> {
                        todoSave()
                        return true
                    }
                }
                return true
            }
        }
    }
}
