package `in`.devco.cr.extensions

import android.view.View
import com.afollestad.materialdialogs.MaterialDialog

fun MaterialDialog.show(enable: Boolean) {
    if (enable) {
        if (!isShowing)
            show()
    } else {
        if (isShowing)
            dismiss()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}