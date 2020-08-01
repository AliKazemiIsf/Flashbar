package ir.awrhm.flashbar.view


import android.content.Context
import android.util.AttributeSet
import ir.awrhm.flashbar.R

internal class FbButton : androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.style.FbButtonStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}