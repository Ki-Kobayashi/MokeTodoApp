package com.example.moketodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moketodoapp.databinding.FragmentMainBinding
import com.example.moketodoapp.model.todo.Todo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn

/**
 * Created by K.Kobayashi on 2023/06/04.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val vm: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("@@@@@ ${TAG}: onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("@@@@@ ${TAG}: onViewCreated")
        initViews(view)
    }

    /**
     * RecycleViewの設定
     *  - Adapterのセット
     *  - LayoutManagerのセット
     *  - 表示取得リストのセット
     *          - 表示取得リストに変更があった場合は、随時更新されるようにする
     */
    private fun initViews(view: View) {
        this._binding = FragmentMainBinding.bind(view)
        binding.apply {
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_createTodoFragment)
            }
            // 一覧表示Adapterのセットアップ
            setupRvAdapter()
        }
    }

    /**
     *  一覧表示Adapterのセットアップ
     */
    private fun setupRvAdapter() {
        // TodoAdapterがラムダの理由：引数が「一つ」＋「関数」のため
        val todoAdapter = TodoAdapter {
            if (findNavController().currentDestination?.id != R.id.mainFragment) return@TodoAdapter
            val action = MainFragmentDirections.actionMainFragmentToDetailTodoFragment(it)
            findNavController().navigate(action)
        }

        binding.rvTodoList.apply {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            // 区切り線
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        vm.todoList.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
            binding.rvTodoList.smoothScrollToPosition(1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}
