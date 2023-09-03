package app.appworks.school.stylish.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.DetailMessage
import app.appworks.school.stylish.databinding.ItemDetailColorBinding
import app.appworks.school.stylish.databinding.ItemDetailMessageBinding

class DetailMessageAdapter : ListAdapter<DetailMessage, DetailMessageAdapter.MessageViewHolder>(DiffCallback()) {

    class MessageViewHolder(val binding: ItemDetailMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(messageData: DetailMessage) {
            messageData.let {
                binding.message = it
                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            ItemDetailMessageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<DetailMessage>() {
    override fun areItemsTheSame(
        oldItem: DetailMessage,
        newItem: DetailMessage
    ): Boolean {
        return oldItem.message == newItem.message
    }

    override fun areContentsTheSame(
        oldItem: DetailMessage,
        newItem: DetailMessage
    ): Boolean {
        return oldItem == newItem
    }


}