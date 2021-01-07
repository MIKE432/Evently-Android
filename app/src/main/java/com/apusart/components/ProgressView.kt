package com.apusart.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.apusart.evently_android.R

class ProgressPoint(context: Context) : View(context) {
    private val activePaint = Paint()
    private val passivePaint = Paint()
    private val lastPaint = Paint()
    var isActive = false
        set(value) {
            if (value == field)
                return
            field = value
            invalidate()
        }
    var isLast = false
        set(value) {
            if (value == field)
                return
            field = value
            invalidate()
        }

    init {

        activePaint.color = ResourcesCompat.getColor(
            resources,
            R.color.primary_900,
            null
        )

        passivePaint.color = ResourcesCompat.getColor(
            resources,
            R.color.cool_grey,
            null
        )

        lastPaint.color = ResourcesCompat.getColor(
            resources,
            R.color.primary_900,
            null
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawRoundRect(0.0F, 0.0F, width.toFloat(), height.toFloat(), 100F, 100F, if (isActive) if (isLast) lastPaint else activePaint else passivePaint)
        }
    }
}

class ProgressView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    private val progressPoints: List<ProgressPoint>

    var current = -1
        set(value) {
            val newValue = value + 1
            if (newValue == field || value == -1) {
                field = newValue
                return
            }

            field = newValue
            progressPoints.forEachIndexed { index, progressPoint ->
                progressPoint.isActive = index < field
                progressPoint.isLast = index == field - 1
            }
        }

    init {
        context.theme
            .obtainStyledAttributes(
                attributeSet,
                R.styleable.ProgressView,
                0, 0
            ).apply {
                val count = getInteger(R.styleable.ProgressView_progress_view_count, 0)
                val c = getInteger(R.styleable.ProgressView_progress_view_current, -1)

                progressPoints = List(count) {
                    val point = ProgressPoint(context)
                    point.id = View.generateViewId()
                    point
                }

                current = c

                progressPoints.forEachIndexed { index, progressPoint ->
                    val params = LayoutParams(0, 0)
                    params.topToTop = 0
                    params.bottomToBottom = 0
                    when (index) {
                        0 -> {
                            params.startToStart = 0
                            params.endToStart = progressPoints[index + 1].id
                        }
                        progressPoints.lastIndex -> {
                            params.startToEnd = progressPoints[index - 1].id
                            params.endToEnd = 0
                        }
                        else -> {
                            params.startToEnd = progressPoints[index - 1].id
                            params.endToStart = progressPoints[index + 1].id
                        }
                    }
                    params.setMargins(10, 10, 10, 10)
                    progressPoint.layoutParams = params
                }

                progressPoints.forEach {
                    addView(it)
                }
            }
    }
}

@BindingAdapter("app:progress_view_current")
fun setProgressCurrent(view: ProgressView, value: Int) {
    view.current = value
}