package com.amirhusseinsoori.apollotask.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


@SuppressLint("CheckResult")
fun loadImage(
    imageUrl: String?,
    imageViewToLoad: ImageView,
    placeHolderId: Int = 0,
    options: RequestOptions? = null,
    progressBar: ProgressBar? = null,
    forceOriginalSize: Boolean = false,
    centerCrop: Boolean = true,
    diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.AUTOMATIC,
    callback: ((Bitmap?, Boolean) -> Unit)? = null
) {
    val glideRequest = getGlideRequest(
        imageViewToLoad.context,
        imageUrl,
        centerCrop
    )
    setPlaceHolder(glideRequest, placeHolderId)
    setOptions(glideRequest, options)
    setGlideListener(
        glideRequest,
        progressBar,
        callback
    )

    progressBar?.visibility = View.VISIBLE

    glideRequest.diskCacheStrategy(diskCacheStrategy)

    glideRequest.transition(BitmapTransitionOptions.withCrossFade())

    if (forceOriginalSize)
        glideRequest.override(Target.SIZE_ORIGINAL)
    glideRequest.into(imageViewToLoad)
}

@SuppressLint("CheckResult")
private fun setGlideListener(
    glideRequest: GlideRequest<Bitmap>,
    progressBar: ProgressBar?,
    callback: ((Bitmap?, Boolean) -> Unit)?
) {
    glideRequest.listener(object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            callback?.invoke(null, false)
            progressBar?.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            callback?.invoke(resource, true)
            progressBar?.visibility = View.GONE
            return false
        }
    })
}

private fun setOptions(glideRequest: GlideRequest<Bitmap>, options: RequestOptions?) {
    options?.let {
        glideRequest.apply(options)
    }
}

@SuppressLint("CheckResult")
private fun setPlaceHolder(glideRequest: GlideRequest<Bitmap>, placeHolderId: Int) {
    if (placeHolderId > 0) {
        glideRequest.placeholder(placeHolderId)
    }
}

private fun getGlideRequest(
    context: Context,
    imageUrl: String?,
    centerCrop: Boolean
): GlideRequest<Bitmap> {
    return if (centerCrop)
        GlideApp.with(context).asBitmap().load(imageUrl).centerCrop()
    else
        GlideApp.with(context).asBitmap().load(imageUrl)
}

fun circleCropTransform() = RequestOptions.circleCropTransform()
fun roundCornerTransform(cornerRadius: Int) =
    RequestOptions().transforms(CenterCrop(), RoundedCorners(cornerRadius))
