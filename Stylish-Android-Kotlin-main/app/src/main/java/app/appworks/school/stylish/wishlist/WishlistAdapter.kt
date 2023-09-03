package app.appworks.school.stylish.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.databinding.ItemWishlistBinding


class WishlistAdapter(
    private val onClickListener: OnClickListener
    ) : ListAdapter<Product, WishlistAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(private var binding: ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.product = product

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return (oldItem.id == newItem.id) &&
                    (oldItem.selectedVariant.colorCode == newItem.selectedVariant.colorCode) &&
                    (oldItem.selectedVariant.size == newItem.selectedVariant.size)
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.amount == newItem.amount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val name = getItem(position)
        holder.bind(name)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(name)
        }
    }

    class OnClickListener(val clickListener: (name: Product) -> Unit) {
        fun onClick(name: Product) = clickListener(name)
    }
}