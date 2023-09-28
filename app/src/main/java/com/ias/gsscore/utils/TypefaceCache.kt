package com.ias.gsscore.utils

import android.content.res.AssetManager
import android.graphics.Typeface
import java.util.*

object TypefaceCache {

    private var typefaceTemp = ""
    private const val BUZZ_FONT_POPPINS_BOLD = "fonts/Poppins_Bold.ttf"
    private const val BUZZ_FONT_POPPINS_LIGHT = "fonts/Poppins_Light.ttf"
    private const val BUZZ_FONT_POPPINS_MEDIUM = "fonts/Poppins_Medium.ttf"
    private const val BUZZ_FONT_POPPINS_REGULAR = "fonts/Poppins_Regular.ttf"
    private const val BUZZ_FONT_POPPINS_SEMI_BOLD = "fonts/Poppins_SemiBold.ttf"
    private const val BUZZ_FONT_POPPINS_THIN = "fonts/Poppins_Thin.ttf"


    private val CACHE = Hashtable<String, Typeface>()

    operator fun get(manager: AssetManager, typefaceCode: Int): Typeface? {
        synchronized(CACHE) {
            val typefaceName = getTypefaceName(typefaceCode)
            if (!CACHE.containsKey(typefaceName)) {
                val t = Typeface.createFromAsset(manager, typefaceName)
                CACHE[typefaceName] =  t
            }
            return CACHE[typefaceName]
        }
    }

    private fun getTypefaceName(typefaceCode: Int): String {
        typefaceTemp = when (typefaceCode) {
            0 -> BUZZ_FONT_POPPINS_BOLD
            1 -> BUZZ_FONT_POPPINS_LIGHT
            2 -> BUZZ_FONT_POPPINS_MEDIUM
            3 -> BUZZ_FONT_POPPINS_REGULAR
            4 -> BUZZ_FONT_POPPINS_SEMI_BOLD
            5 -> BUZZ_FONT_POPPINS_THIN

            else -> BUZZ_FONT_POPPINS_REGULAR
        }
        return typefaceTemp
    }
}
