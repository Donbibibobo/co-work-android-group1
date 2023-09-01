package app.appworks.school.stylish.add2cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.databinding.ItemAdd2cartColorBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Color], including computing diffs between lists.
 * @param viewModel: [Add2cartViewModel]
 */
class Add2cartColorAdapter(val viewModel: Add2cartViewModel) : ListAdapter<Color, Add2cartColorAdapter.ColorViewHolder>(DiffCallback) {

    private lateinit var context: Context

    /**
     * Implements [LifecycleOwner] to support Data Binding
     */
    class ColorViewHolder(
        private val binding: ItemAdd2cartColorBinding,
        private val viewModel: Add2cartViewModel
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        val isSelected: LiveData<Boolean> = viewModel.selectedColorPosition.map {
            it == adapterPosition
        }

        fun bind(color: Color) {
            binding.lifecycleOwner = this
            binding.color = color
            binding.viewHolder = this
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun markAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun markDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Color>() {
        override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        context = parent.context
        return ColorViewHolder(
            ItemAdd2cartColorBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ColorViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: ColorViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }
}
