package `in`.devco.cr.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadUri(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .into(this)
}

fun ImageView.loadVideoThumbnail(uri: Uri?) {
    Glide.with(this)
        .asBitmap()
        .load(uri)
        .into(this)
}