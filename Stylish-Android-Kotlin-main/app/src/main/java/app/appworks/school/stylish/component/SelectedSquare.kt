package app.appworks.school.stylish.component

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import app.appworks.school.stylish.R
import app.appworks.school.stylish.StylishApplication

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Draw a square border to express be selected
 */
class SelectedSquare(isSelected: Boolean = false) : ShapeDrawable(object : Shape() {
    override fun draw(canvas: Canvas, paint: Paint) {

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
