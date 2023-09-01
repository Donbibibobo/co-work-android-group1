package app.appworks.school.stylish.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.media.ThumbnailUtils
import android.util.AttributeSet
import android.view.View
import app.appworks.school.stylish.R
import app.appworks.school.stylish.component.Particle.Companion.PARTICLE_COUNT
import java.util.*

/**
 * Created by Wayne Chen on 2019-08-01.
 */
class ParticleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val random = Random()

    private val randomNum: Float
        get() {
            random.nextFloat().let {
                return when {
                    it < 0.15f -> it + 0.15f
                    it >= 0.85f -> it - 0.15f
                    else -> it
                }
            }
        }

    private val ptcPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.purple_voyage)
        alpha = Particle.ALPHA_MAX
        isDither = true
        style = Paint.Style.FILL
    }
    private val ptcMatrix = Matrix()

    private val ptcBitmap: Bitmap = R.drawable.stylish.decodeBitmap(context)

    private var ptcMeasuredWidth = 0
    private var ptcMeasuredHeight = 0

    private val particles: MutableList<Particle> = mutableListOf()

    var particleAnim: ValueAnimator

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)

        particleAnim = ValueAnimator.ofInt(0).setDuration(30)
        particleAnim.repeatCount = ValueAnimator.INFINITE
        particleAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                invalidate()
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (ptcMeasuredWidth == 0) {
            ptcMeasuredWidth = measuredWidth
            ptcMeasuredHeight = measuredHeight
        }

        if (particles.size == 0) {
            for (i in 0 until PARTICLE_COUNT) {
                particles.add(
                    Particle(
                        ptcBitmap,
                        ptcMatrix,
                        ptcPaint,
                        ptcMeasuredWidth / 2f,
                        ptcMeasuredHeight / 2f,
//                        randomNum * ptcMeasuredWidth,
//                        randomNum * ptcMeasuredHeight,
                        ptcMeasuredWidth,
                        ptcMeasuredHeight
                    )
                )
            }
        }
//        if (!particleAnim.isRunning) {
//            Logger.d("onMeasure: start animation}")
//            particleAnim.start()
//        }
    }

    fun startAnimation() {
        particleAnim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        for (particle in particles) {
            particle.draw(canvas)
        }
        canvas.restore()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        particleAnim.end()
    }
}

private fun Int.decodeBitmap(context: Context): Bitmap {

    val options = BitmapFactory.Options()
    options.inPreferredConfig = Bitmap.Config.RGB_565

    return ThumbnailUtils.extractThumbnail(
        BitmapFactory.decodeResource(context.resources, this, options),
        context.resources.getDimensionPixelSize(R.dimen.size_particle_logo),
        context.resources.getDimensionPixelSize(R.dimen.size_particle_logo),
        ThumbnailUtils.OPTIONS_RECYCLE_INPUT
    )
}

class Particle(
    private val bitmap: Bitmap,
    private val matrix: Matrix,
    private val paint: Paint,
    private var x: Float,
    private var y: Float,
    val width: Int,
    val height: Int
) {

    private val centerX = bitmap.width / 2f
    private val centerY = bitmap.height / 2f
    private var degrees: Float = 0f

    private val random = Random()
    var dX: Float = 0f
    var dY: Float = 0f
    var addDegrees: Float = 0f
    var isAddX: Boolean
    var isAddY: Boolean

    init {

        isAddX = random.nextBoolean()
        isAddY = random.nextBoolean()

        setupRandomParam()
    }

    private fun setupRandomParam() {
        dX = random.nextInt(2) + 1.2f
        dY = random.nextInt(2) + 1.2f
        addDegrees = random.nextInt(5) + 3f
    }

    private fun pnValue(isAdd: Boolean, value: Float): Float {
        return when (isAdd) {
            true -> value
            else -> 0 - value
        }
    }

    fun draw(canvas: Canvas) {
        matrix.reset()
        x = x.plus(pnValue(isAddX, dX) * 3)
        y = y.plus(pnValue(isAddY, dY) * 3)
        matrix.preTranslate(
            x,
            y
        )
        degrees = degrees.plus(addDegrees)
        matrix.preRotate(
            degrees,
            centerX,
            centerY
        )
        canvas.drawBitmap(bitmap, matrix, paint)
        judgeOutline()
    }

    private fun judgeOutline() {

        val judgeX = x <= 0 || x >= width - bitmap.width
        val judgeY = y <= 0 || y >= height - bitmap.height

        when {
            judgeX -> {
                isAddX = !isAddX
                isAddY = random.nextBoolean()
                setupRandomParam()
                x = if (x <= 0) {
                    0f
                } else {
                    (width - bitmap.width).toFloat()
                }
            }
            judgeY -> {
                isAddY = !isAddY
                isAddX = random.nextBoolean()
                setupRandomParam()
                y = if (y <= 0) {
                    0f
                } else {
                    (height - bitmap.height).toFloat()
                }
            }
        }
    }

    companion object {
        const val ALPHA_MAX = 200
        const val ALPHA_MIN = 75
        const val PARTICLE_COUNT = 30
    }
}
