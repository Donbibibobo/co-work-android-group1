package app.appworks.school.stylish.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.FragmentHistoryBinding
import app.appworks.school.stylish.databinding.ItemWishlistBinding
import java.time.LocalDateTime

class HistoryFragment : Fragment() {


    private lateinit var viewModel: HistoryViewModel

    private val mockDataList = mutableListOf<History>(
        History(
            LocalDateTime.of(2023, 9, 1, 14, 30),
            "123456",
            100,
            "Remark 1"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_history, container, false)

        val binding =FragmentHistoryBinding.inflate(inflater)
        val adapter = HistoryAdapter()
        binding.historyRecyclerView.adapter = adapter
        adapter.submitList(mockDataList)
//        binding.historyRecyclerView

        return binding.root
    }

}