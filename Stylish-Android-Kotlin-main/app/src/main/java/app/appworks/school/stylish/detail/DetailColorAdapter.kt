package app.appworks.school.stylish.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.databinding.ItemDetailColorBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * [Color], including computing diffs between lists.
 */
class DetailColorAdapter : ListAdapter<Color, DetailColorAdapter.ColorViewHolder>(DiffCallback) {

    class ColorViewHolder(private var binding: ItemDetailColorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Color) {
            color.let {
                binding.color = it
                binding.executePendingBindings()
            }
        }
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
        return ColorViewHolder(
            ItemDetailColorBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
