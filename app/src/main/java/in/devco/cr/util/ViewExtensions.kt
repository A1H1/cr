package `in`.devco.cr.util

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