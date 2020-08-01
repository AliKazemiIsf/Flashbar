package ir.awrhm.flashbar.view

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import ir.awrhm.flashbar.R

internal class ShadowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal fun applyShadow(type: ShadowType) {
        when (type) {
            ShadowType.TOP -> setShadow(R.drawable.shadow_top)
            ShadowType.BOTTOM -> setShadow(R.drawable.shadow_bottom)
        }
    }

    private fun setShadow(@DrawableRes id: Int) {
        val shadow = ContextCompat.getDrawable(context, id)
        if (SDK_INT >= JELLY_BEAN) {
            this.background = shadow
        } else {
            this.setBackgroundDrawable(shadow)
        }
    }

    enum class ShadowType {
        TOP, BOTTOM
    }
}