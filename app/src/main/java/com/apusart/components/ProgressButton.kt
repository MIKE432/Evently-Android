package com.apusart.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.apusart.evently_android.R
import kotlinx.android.synthetic.main.main_input.view.*
import kotlinx.android.synthetic.main.progress_button.view.*

class ProgressButton(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {
    private val view = LayoutInflater.from(context)
        .inflate(R.layout.progress_button, this, false)

    var isDisabled: Boolean = false
        set(value) {
            if (value == field)
                return
            field = value
            view.progress_button_button.isEnabled = !field
        }

    var title = ""
        set(value) {
            if (value != field) {
                field = value
                view.progress_button_button.text = field
            }
        }

    init {
        addView(view)
        context.theme.obtainStyledAttributes(attributeSet,
        R.styleable.ProgressButton,
        0,0).apply {
            val buttonTitle = getText(R.styleable.ProgressButton_button_name)

            if (buttonTitle != null) {
                title = buttonTitle.toString()
            }
        }
    }

    fun transitionToEnd() {
        (view as MotionLayout).transitionToEnd()
    }

    fun transitionToStart() {
        (view as MotionLayout).transitionToStart()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        view.progress_button_button.setOnClickListener(l)
    }
}