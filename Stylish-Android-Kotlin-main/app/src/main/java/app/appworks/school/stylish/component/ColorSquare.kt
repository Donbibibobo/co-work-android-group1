package app.appworks.school.stylish.component

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Draw a color square with border
 * @param colorCode: format is '#FFFFFF'
 * @param isSelected: when value is 'true', draw a square border to express be selected
 * @param hasBorder: default is true for Stylish
 */
class ColorSquare(colorCode: String, isSelected: Boolean = false, hasBorder: Boolean = true) : ShapeDrawable(object : Shape() {
    override fun draw(canvas: Canvas, paint: Paint) {

        paint.color = Color.parseColor(colorCode)
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, this.width, this.height, paint)

        if (hasBorder) {
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = StylishApplication.instance.resources
                .getDimensionPixelSize(R.dimen.edge_detail_color).toFloat()
            canvas.drawRect(0f, 0f, this.width, this.height, paint)
        }

        if (isSelected) {
            if (colorCode.contains("FFFFFF", true)) {
                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_white).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            } else {
                paint.color = Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_white).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)

                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = StylishApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.edge_add2cart_color_black).toFloat()
                canvas.drawRect(0f, 0f, this.width, this.height, paint)
            }
        }
    }
})
