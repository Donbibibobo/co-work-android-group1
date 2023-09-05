package app.appworks.school.stylish

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.add2cart.Add2cartColorAdapter
import app.appworks.school.stylish.add2cart.Add2cartSizeAdapter
import app.appworks.school.stylish.add2cart.Add2cartViewModel
import app.appworks.school.stylish.cart.CartAdapter
import app.appworks.school.stylish.catalog.item.CatalogItemAdapter
import app.appworks.school.stylish.component.ColorSquare
import app.appworks.school.stylish.component.SelectedSquare
import app.appworks.school.stylish.data.Color
import app.appworks.school.stylish.data.HomeItem
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.Variant
import app.appworks.school.stylish.detail.DetailCircleAdapter
import app.appworks.school.stylish.detail.DetailColorAdapter
import app.appworks.school.stylish.detail.DetailGalleryAdapter
import app.appworks.school.stylish.home.HomeAdapter
import app.appworks.school.stylish.network.LoadApiStatus
import app.appworks.school.stylish.payment.PaymentAdapter
import app.appworks.school.stylish.util.Util.getColor
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Wayne Chen in Jul. 2019.
 */
@BindingAdapter("homeItems")
fun bindRecyclerViewWithHomeItems(recyclerView: RecyclerView, homeItems: List<HomeItem>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("products")
fun bindRecyclerViewWithProducts(recyclerView: RecyclerView, products: List<Product>?) {
    products?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is CatalogItemAdapter -> submitList(it)
                is CartAdapter -> {
                    when (itemCount) {
                        0 -> submitList(it)
                        it.size -> notifyDataSetChanged()
                        else -> submitList(it)
                    }
                }

                is PaymentAdapter -> submitProducts(products)
            }
        }
    }
}

@BindingAdapter("images")
fun bindRecyclerViewWithImages(recyclerView: RecyclerView, images: List<String>?) {
    images?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is DetailGalleryAdapter -> {
                    submitImages(it)
                }
            }
        }
    }
}

@BindingAdapter("colors")
fun bindRecyclerViewWithColors(recyclerView: RecyclerView, colors: List<Color>?) {
    colors?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is DetailColorAdapter -> submitList(it)
                is Add2cartColorAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("count")
fun bindRecyclerViewByCount(recyclerView: RecyclerView, count: Int?) {
    count?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is DetailCircleAdapter -> {
                    submitCount(it)
                }
            }
        }
    }
}

@BindingAdapter("sizes", "viewModel")
fun bindAdd2cartSizesRecyclerView(
    recyclerView: RecyclerView,
    variants: List<Variant>?,
    viewModel: Add2cartViewModel
) {
    variants?.let {
        recyclerView.adapter = Add2cartSizeAdapter(viewModel).apply {
            submitList(it)
        }
    }
}

// Draw Square

@BindingAdapter("color")
fun drawColorSquare(imageView: ImageView, color: Color?) {
    color?.let {
        imageView.background = ColorSquare("#${it.code}")
    }
}

@BindingAdapter("color")
fun drawColorSquare(imageView: ImageView, colorCode: String?) {
    colorCode?.let {
        imageView.background = ColorSquare("#$colorCode")
    }
}

@BindingAdapter("color", "selected")
fun drawColorSquareBySelected(imageView: ImageView, color: Color?, isSelected: Boolean = false) {
    color?.let {
        imageView.background = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {

                paint.color = android.graphics.Color.parseColor("#${it.code}")
                paint.style = Paint.Style.FILL
                canvas.drawRect(0f, 0f, this.width, this.height, paint)

                if (isSelected) {
                    paint.color = android.graphics.Color.WHITE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = StylishApplication.instance.resources
                        .getDimensionPixelSize(R.dimen.edge_add2cart_color_white).toFloat()
                    canvas.drawRect(0f, 0f, this.width, this.height, paint)

                    paint.color = android.graphics.Color.BLACK
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = StylishApplication.instance.resources
                        .getDimensionPixelSize(R.dimen.edge_add2cart_color_black).toFloat()
                    canvas.drawRect(0f, 0f, this.width, this.height, paint)
                }
            }
        })



        ColorSquare("#${it.code}", isSelected = isSelected)
    }
}

@BindingAdapter("selected")
fun drawAdd2cartSizeSelectedSquare(textView: TextView, isSelected: Boolean?) {
    textView.foreground = SelectedSquare(isSelected ?: false)
}

// Add2cart Editor

@BindingAdapter("amount", "stock")
fun bindAdd2cartEditorStatus(editText: EditText, amount: Long, stock: Int) {

    editText.apply {
        background = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {

                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_add2cart_select).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            }
        })

        when (stock) {
            1 -> {
                if (text.toString() != stock.toString()) {
                    setText(stock.toString())
                }
                isClickable = false
                isFocusable = false
                isFocusableInTouchMode = false
                backgroundTintList = ColorStateList.valueOf(getColor(R.color.gray_999999))
                setTextColor(getColor(R.color.gray_999999))
            }

            else -> {
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = true
                backgroundTintList = ColorStateList.valueOf(getColor(R.color.black_3f3a3a))
                when {
                    amount > stock -> setTextColor(getColor(R.color.red_d0021b))
                    else -> setTextColor(getColor(R.color.black_3f3a3a))
                }
            }
        }
    }
}

@BindingAdapter("editorControllerStatus")
fun bindEditorControllerStatus(imageButton: ImageButton, enabled: Boolean) {

    imageButton.apply {
        foreground = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {

                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_add2cart_select).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            }
        })
        isClickable = enabled
        backgroundTintList = ColorStateList.valueOf(
            getColor(
                when (enabled) {
                    true -> R.color.black_3f3a3a
                    false -> R.color.gray_999999
                }
            )
        )
        foregroundTintList = ColorStateList.valueOf(
            getColor(
                when (enabled) {
                    true -> R.color.black_3f3a3a
                    false -> R.color.gray_999999
                }
            )
        )
    }
}

@BindingAdapter("amount", "stock")
fun bindEditorStatus(textView: TextView, amount: Long, stock: Int) {
    textView.apply {
        background = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {

                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_cart_select).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            }
        })
    }
}

@BindingAdapter("circleStatus")
fun bindDetailCircleStatus(imageView: ImageView, isSelected: Boolean = false) {
    imageView.background = ShapeDrawable(object : Shape() {
        override fun draw(canvas: Canvas, paint: Paint) {

            paint.color = getColor(R.color.white)
            paint.isAntiAlias = true

            when (isSelected) {
                true -> {
                    paint.style = Paint.Style.FILL
                }

                false -> {
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = StylishApplication.instance.resources
                        .getDimensionPixelSize(R.dimen.edge_detail_circle).toFloat()
                }
            }

            canvas.drawCircle(
                this.width / 2, this.height / 2,
                StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.radius_detail_circle).toFloat(),
                paint
            )
        }
    })
}

@BindingAdapter("itemPosition", "itemCount")
fun setupPaddingForGridItems(layout: ConstraintLayout, position: Int, count: Int) {

    val outsideHorizontal =
        StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_outside_horizontal_catalog_item)
    val insideHorizontal =
        StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_inside_horizontal_catalog_item)
    val outsideVertical =
        StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_outside_vertical_catalog_item)
    val insideVertical =
        StylishApplication.instance.resources.getDimensionPixelSize(R.dimen.space_inside_vertical_catalog_item)

    val layoutParams = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    )

    when {
        position == 0 -> { // first item and confirm whether only 1 row
            layoutParams.setMargins(
                outsideHorizontal,
                outsideVertical,
                insideHorizontal,
                if (count > 2) insideVertical else outsideVertical
            )
        }

        position == 1 -> { // second item and confirm whether only 1 row
            layoutParams.setMargins(
                insideHorizontal,
                outsideVertical,
                outsideHorizontal,
                if (count > 2) insideVertical else outsideVertical
            )
        }

        count % 2 == 0 && position == count - 1 -> { // count more than 2 and item count is even
            layoutParams.setMargins(
                insideHorizontal,
                insideVertical,
                outsideHorizontal,
                outsideVertical
            )
        }

        (count % 2 == 1 && position == count - 1) || (count % 2 == 0 && position == count - 2) -> {
            layoutParams.setMargins(
                outsideHorizontal,
                insideVertical,
                insideHorizontal,
                outsideVertical
            )
        }

        position % 2 == 0 -> { // even
            when (position) {
                count - 1 -> layoutParams.setMargins(
                    insideHorizontal,
                    insideVertical,
                    outsideHorizontal,
                    outsideVertical
                ) // last 1
                count - 2 -> layoutParams.setMargins(
                    outsideHorizontal,
                    insideVertical,
                    insideHorizontal,
                    outsideVertical
                ) // last 2
                else -> layoutParams.setMargins(
                    outsideHorizontal,
                    insideVertical,
                    insideHorizontal,
                    insideVertical
                )
            }
        }

        position % 2 == 1 -> { // odd
            when (position) {
                count - 1 -> layoutParams.setMargins(
                    outsideHorizontal,
                    insideVertical,
                    insideHorizontal,
                    outsideVertical
                ) // last 1
                else -> layoutParams.setMargins(
                    insideHorizontal,
                    insideVertical,
                    outsideHorizontal,
                    insideVertical
                )
            }
        }
    }

    layout.layoutParams = layoutParams
}

// General

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().build()
        GlideApp.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
            )
            .into(imgView)
    }
}

/**
 * Adds decoration to [RecyclerView]
 */
@BindingAdapter("addDecoration")
fun bindDecoration(recyclerView: RecyclerView, decoration: RecyclerView.ItemDecoration?) {
    decoration?.let { recyclerView.addItemDecoration(it) }
}

/**
 * Displays currency price to [TextView] by [Int]
 */
@BindingAdapter("price")
fun bindPrice(textView: TextView, price: Int?) {
    price?.let { textView.text = StylishApplication.instance.getString(R.string.nt_dollars_, it) }
}

/**
 * Displays currency price to [TextView] by [Long]
 */
@BindingAdapter("price")
fun bindPrice(textView: TextView, price: Long?) {
    price?.let { textView.text = StylishApplication.instance.getString(R.string.nt_dollars_, it) }
}

/**
 * According to [LoadApiStatus] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
        else -> view.visibility = View.GONE
    }
}

/**
 * According to [message] to decide the visibility of [ProgressBar]
 */
@BindingAdapter("setupApiErrorMessage")
fun bindApiErrorMessage(view: TextView, message: String?) {
    when (message) {
        null, "" -> {
            view.visibility = View.GONE
        }

        else -> {
            view.text = message
            view.visibility = View.VISIBLE
        }
    }
}

/**
 * According to [colorCode] to draw [ShapeDrawable] for the background of [ImageView]
 */
@BindingAdapter("colorCode")
fun bindColorByColorCode(imageView: ImageView, colorCode: String?) {
    colorCode?.let {
        imageView.background = ShapeDrawable(object : Shape() {
            override fun draw(canvas: Canvas, paint: Paint) {

                paint.color = android.graphics.Color
                    .parseColor("#$colorCode")
                paint.style = Paint.Style.FILL
                canvas.drawRect(0f, 0f, this.width, this.height, paint)

                paint.color = android.graphics.Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_detail_color).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            }
        })
    }
}

@BindingAdapter("ratings")
fun bindRatings(textView: TextView, ratings: String?) {
    ratings?.let {
        if (it != "-1") {
            val floatValue = it.toFloat()
            val roundedValue = Math.round(floatValue * 10.0) / 10.0
            textView.text = roundedValue.toString()
        } else {
            textView.text = "無評分"
        }
    }
}

@BindingAdapter("rateAmount")
fun bindRateAmount(textView: TextView, rateAmount: Int?) {
    rateAmount?.let {
        if(it != 0){
        textView.text = StylishApplication.instance.getString(R.string.rate_amount, it)
    }
        else{
            textView.text = ""
        }
    }
}
