package com.apusart.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.apusart.evently_android.R
import kotlinx.android.synthetic.main.header.view.*

class HeaderView(context: Context, attributeSet: AttributeSet): CardView(context, attributeSet) {
    private val view = LayoutInflater.from(context)
        .inflate(R.layout.header, this, false)

    var title: String? = null
        set(value) {
            view.header_title.text = value
            field = value
        }

    var subtitle: String? = null
        set(value) {
            view.header_subtitle.isVisible = value != null && value != ""
            view.header_subtitle.text = value
            field = value
        }

    var iconLeading: Drawable? = null
        set(value) {
            view.header_leading_icon.visibility = if (value != null) View.VISIBLE else View.VISIBLE
            view.header_leading_icon.setImageDrawable(value)
            field = value
        }

    var iconTrailing: Drawable? = null
        set(value) {
            view.header_trailing_icon.visibility = if (value != null) View.VISIBLE else View.VISIBLE
            view.header_trailing_icon.setImageDrawable(value)
            field = value
        }


    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.HeaderView,
            0, 0
        ).apply {
            title = getText(R.styleable.HeaderView_header_view_title)?.toString()
            subtitle = getText(R.styleable.HeaderView_header_view_subtitle)?.toString()
            iconLeading = getDrawable(R.styleable.HeaderView_header_view_leading_icon)
            iconTrailing = getDrawable(R.styleable.HeaderView_header_view_trailing_icon)
        }
        elevation = 50f
        addView(view)
    }

    fun setOnLeadingIconClickListener(l:(View) -> Unit) {
        view.header_leading_icon.setOnClickListener(l)
    }

    fun setOnTrailingIconClickListener(l:(View) -> Unit) {
        view.header_trailing_icon.setOnClickListener(l)
    }
}