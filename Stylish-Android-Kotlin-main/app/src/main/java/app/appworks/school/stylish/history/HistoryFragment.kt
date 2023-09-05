package app.appworks.school.stylish.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentHistoryBinding
import app.appworks.school.stylish.databinding.ItemWishlistBinding
import app.appworks.school.stylish.payment.OrderDataClass
import java.time.LocalDateTime

class HistoryFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_history, container, false)
        val viewModel= HistoryViewModel()
        val binding =FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val adapter = HistoryAdapter()
        binding.historyRecyclerView.adapter = adapter
        viewModel.orderHistory.observe(viewLifecycleOwner,Observer{
            adapter.submitList(viewModel.orderHistory.value)

        })
//        adapter.submitList(viewModel.orderHistory.value)

        Log.i("HISTORY3","${viewModel.orderHistory.value}")
        return binding.root
    }

}