package com.example.commons.extensions

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


/** My extensions functions **/


/**    Extensions for Views    **/

fun View.hide(hide: Boolean = true): Int {
    visibility = if (hide) View.GONE else show()
    return visibility
}

fun View.show(show: Boolean = true): Int{
    visibility = if (show) View.VISIBLE else hide()
    return visibility
}

/**    Extensions for Colors    **/

fun Activity.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)


/**    Extensions for Any Null?    **/

fun Any?.isNull() = this == null


/**    Extensions for Toast    **/

fun Activity.toast(text:String, length:Int = Toast.LENGTH_LONG){
    Toast.makeText(this, text, length).show()
}


/**    Extensions for glide    **/

/** need coroutines implementation
 * => implementation('org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0')
 */
fun ImageView.load(url:String){
    val myThis = this
    CoroutineScope(Dispatchers.IO).launch {
        if (url.isNotEmpty()){
            Glide.with(myThis.context).load(url).into(myThis)
        }
    }
}


/**    Extension for native load URL image    **/

/** need coroutines implementation
 * => implementation('org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0')
 */
fun ImageView.setItemFromURL(imageURL: String?, rounded:Boolean = false){
    val myThis = this
    CoroutineScope(Dispatchers.IO).launch {
        if (imageURL != null) {
            try {
                val url = URL(imageURL).openStream()
                val image = BitmapFactory.decodeStream(url)
                handler.post{
                    if (rounded) {
                        val bitmapRound = RoundedBitmapDrawableFactory.create(resources, image)
                        bitmapRound.cornerRadius = 1000f
                        myThis.setImageDrawable(bitmapRound)
                    } else {
                        myThis.setImageBitmap(image)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


/**    Extensions for EditTexts    **/

fun EditText.onTextChanged(listener:(String) -> Unit){
    this.addTextChangedListener(object : TextWatcher{

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(s: Editable?) { listener(s.toString()) }

    })
}


/**    Extensions for Dates    **/

fun Date?.customFormat() : String? {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault())
    if (this != null)
        return formatter.format(this)
    return null
    /** Example of use
     * val myDate = Date()
     * println(myDate.customFormat())
     **/
}

/** Constant */
val Date?.formatSize : Int
    get() = this.customFormat()?.length ?: 0