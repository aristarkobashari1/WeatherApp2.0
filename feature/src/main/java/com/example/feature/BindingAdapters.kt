package com.example.feature

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@BindingAdapter("loadWeatherImage")
fun loadWeatherImage(imageView: ImageView, imageUrl: String?) {
    val openWeatherApiUrl = "https://openweathermap.org/img/wn/${imageUrl}@2x.png"
    loadImage(imageView,openWeatherApiUrl)
}

@BindingAdapter("loadImage")
fun loadImage(imageView: ImageView,imageUrl: String?){
    Glide.with(imageView.context)
        .load(imageUrl)
        .into(imageView)
}



@BindingAdapter("loadDrawable")
fun loadDrawable(textView: TextView, imageUrl: String?){
    val openWeatherApiUrl = "https://openweathermap.org/img/wn/${imageUrl}@2x.png"
    openWeatherApiUrl.let {
        Glide.with(textView.context)
            .load(it)
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle cleanup when the drawable is no longer needed
                }
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    // Set the loaded image as the left drawable of the TextView
                    textView.setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
                }
            })
    }
}