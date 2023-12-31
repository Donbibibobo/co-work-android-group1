package app.appworks.school.stylish.detail

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.ItemDetailGalleryBinding

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class DetailGalleryAdapter : RecyclerView.Adapter<DetailGalleryAdapter.ImageViewHolder>() {

    private lateinit var context: Context
    // the data of adapter
    private var images: List<String>? = null

    class ImageViewHolder(private var binding: ItemDetailGalleryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, imageUrl: String) {

            imageUrl.let {
                binding.imageUrl = it

                // Make sure the size of each image item can display correct
                val displayMetrics = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                binding.imageDetailGallery.layoutParams = ConstraintLayout.LayoutParams(
                    displayMetrics.widthPixels,
                    context.resources.getDimensionPixelSize(R.dimen.height_detail_gallery)
                )

                binding.executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        context = parent.context
        return ImageViewHolder(
            ItemDetailGalleryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        images?.let {
            holder.bind(context, it[getRealPosition(position)])
        }
    }

    override fun getItemCount(): Int {
        return images?.let { Int.MAX_VALUE } ?: 0
    }

    private fun getRealPosition(position: Int): Int = images?.let {
        position % it.size
    } ?: 0

    /**
     * Submit data list and refresh adapter by [notifyDataSetChanged]
     * @param images: [List] [String]
     */
    fun submitImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }
}
