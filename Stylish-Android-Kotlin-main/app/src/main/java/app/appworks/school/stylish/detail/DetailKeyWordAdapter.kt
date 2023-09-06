package app.appworks.school.stylish.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.databinding.ItemDetailKeyWordBinding

class DetailKeyWordAdapter: ListAdapter<String, DetailKeyWordAdapter.KeyWordViewHolder>(DiffCallback) {

    class KeyWordViewHolder(private var binding: ItemDetailKeyWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(keyword: String) {
            keyword.let {
                binding.viewModel = it
                binding.executePendingBindings()
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyWordViewHolder {
        return KeyWordViewHolder(
            ItemDetailKeyWordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: KeyWordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
