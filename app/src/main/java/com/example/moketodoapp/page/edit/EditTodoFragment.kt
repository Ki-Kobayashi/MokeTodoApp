package com.example.moketodoapp.page.edit

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moketodoapp.R
import com.example.moketodoapp.databinding.FragmentEditTodoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by K.Kobayashi on 2023/06/04.
 *
 * ★TODO:【モケラボ】 Fragment 生成の書き方（継承Fragmentのコンストラクタ使用）
 */
@AndroidEntryPoint
class EditTodoFragment : Fragment(R.layout.fragment_edit_todo) {
    private val vm: EditTodoViewModel by viewModels()

    private val args: EditTodoFragmentArgs by navArgs()

    // 「_xxx」：一時的に格納する変数という意味
    private var _binding: FragmentEditTodoBinding? = null
    private val binding: FragmentEditTodoBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().addMenuProvider(
            createMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setObserver()
    }

    private fun initViews(view: View) {
        this._binding = FragmentEditTodoBinding.bind(view)
        val todo = args.todo
        binding.apply {
            inputEditTitle.setText(todo.title)
            inputEditContent.setText(todo.detail)
        }
    }

    private fun setObserver() {
        vm.apply {
            errMessage.observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) return@observe
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                errMessage.value = ""
            }
            successData.observe(viewLifecycleOwner) { updateTodo ->
                setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to updateTodo))
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }


    private fun createMenu(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_edit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_done -> {
                        // TODO: メニューのDB登録
                        vm.updateTodo(
                            args.todo,
                            binding.inputEditTitle.text.toString(),
                            binding.inputEditContent.text.toString()
                        )
                        // TODO; 完了後、一覧画面へ戻る
                    }
                }
                return true
            }
        }
    }

    companion object {
        private val TAG = EditTodoFragment::class.java.simpleName
        const val REQUEST_KEY = "update_success"
        const val BUNDLE_KEY = "todo"
    }
}
