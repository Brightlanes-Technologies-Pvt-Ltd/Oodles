package com.ias.gsscore.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.ias.gsscore.R


class CustomTextView : AppCompatTextView {

    private var typefaceCode: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            // Get Custom Attribute Name and value
            val styledAttrs = context.obtainStyledAttributes(
                attrs,
                R.styleable.BuzzFonts
            )
            typefaceCode = styledAttrs.getInt(R.styleable.BuzzFonts_fontType, -1)
            val typeface = TypefaceCache[context.assets, typefaceCode]
            setTypeface(typeface)

            styledAttrs.recycle()
            if (isInEditMode) {
                return
            }
        }
    }

    constructor(context: Context) : super(context)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setText(text1: CharSequence?, type: BufferType) {
        var text = text1
        if (text != null) {
            text = Html.fromHtml(text.toString(),Html.FROM_HTML_MODE_COMPACT)
        }
        super.setText(text, type)
    }
}
