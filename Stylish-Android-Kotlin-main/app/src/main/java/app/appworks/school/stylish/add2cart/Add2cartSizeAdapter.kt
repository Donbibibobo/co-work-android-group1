package app.appworks.school.stylish.add2cart

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Variant
import app.appworks.school.stylish.databinding.ItemAdd2cartSizeBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Variant], including computing diffs between lists.
 * @param viewModel: [Add2cartViewModel]
 */
class Add2cartSizeAdapter(val viewModel: Add2cartViewModel) : ListAdapter<Variant, Add2cartSizeAdapter.SizeViewHolder>(DiffCallback) {

    private lateinit var context: Context

    class SizeViewHolder(
        private val binding: ItemAdd2cartSizeBinding,
        private val viewModel: Add2cartViewModel
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        val isSelected: LiveData<Boolean> = viewModel.selectedVariantPosition.map {
            it == adapterPosition
        }

        fun bind(variant: Variant) {
            binding.lifecycleOwner = this
            binding.variant = variant
            binding.viewHolder = this
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override val lifecycle: Lifecycle
            get() = lifecycleRegistry
    }

    override fun onViewAttachedToWindow(holder: SizeViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: SizeViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Variant>() {
        override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        context = parent.context
        return SizeViewHolder(
            ItemAdd2cartSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
