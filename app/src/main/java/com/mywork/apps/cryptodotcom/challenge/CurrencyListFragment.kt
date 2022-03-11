package com.mywork.apps.cryptodotcom.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mywork.apps.cryptodotcom.challenge.adapters.CurrencyListAdapter
import com.mywork.apps.cryptodotcom.challenge.databinding.FragmentItemListBinding
import com.mywork.apps.cryptodotcom.challenge.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private val currencyListAdapter by lazy { CurrencyListAdapter(viewModel::selectedCurrency) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemListBinding.inflate(inflater, container, false)

        with(binding.list) {
            val orientation = (layoutManager as LinearLayoutManager).orientation
            addItemDecoration(DividerItemDecoration(context, orientation))
            adapter = currencyListAdapter
        }

        initLiveData()
        return binding.root
    }

    private fun initLiveData() {
        viewModel.listView.observe(viewLifecycleOwner) {
            if (it is MainViewModel.ListResult.Success) {
                currencyListAdapter.submitList(it.data)
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CurrencyListFragment().apply {
                // arguments = Bundle().apply {
                //     putInt(ARG_COLUMN_COUNT, columnCount)
                // }
            }
    }
}