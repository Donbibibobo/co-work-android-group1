package app.appworks.school.stylish.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.databinding.ItemChatReceivedBinding
import app.appworks.school.stylish.databinding.ItemChatSentBinding


private const val ITEM_SENT_MSG = 0x00
private const val ITEM_RECEIVED_MSG = 0x01

class ChatAdapter : ListAdapter<ChatDataClass, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is ChatDataClass.Sent -> ITEM_SENT_MSG
            is ChatDataClass.Received -> ITEM_RECEIVED_MSG
        }
    }

    class ChatSentViewHolder(private var binding: ItemChatSentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ChatDataClass) {
            binding.viewModel = product
            binding.executePendingBindings()
        }
    }

    class ChatReceiveViewHolder(private var binding: ItemChatReceivedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ChatDataClass) {
            binding.viewModel = product
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ChatDataClass>() {
        override fun areItemsTheSame(oldItem: ChatDataClass, newItem: ChatDataClass): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatDataClass, newItem: ChatDataClass): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_SENT_MSG -> ChatSentViewHolder(
               ItemChatSentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
           ITEM_RECEIVED_MSG -> ChatReceiveViewHolder(
                ItemChatReceivedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatSentViewHolder -> {
                holder.bind((getItem(position) as ChatDataClass.Sent))
            }
            is ChatReceiveViewHolder -> {
                holder.bind((getItem(position) as ChatDataClass.Received))
            }
        }
    }
}