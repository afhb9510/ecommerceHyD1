package com.example.ecommerce.common
import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.R


class loader(private val context: Context, view: View) {

    private val viewGroup = view.findViewById<ViewGroup>(android.R.id.content)
    private val dialogView= LayoutInflater.from(context).inflate(R.layout.loader, viewGroup, false)
    private val builder = AlertDialog.Builder(context).setView(dialogView)
    private var alertDialog=builder.create()
    private val anim: LottieAnimationView = dialogView.findViewById(R.id.anim)
    fun show() {
        anim.playAnimation()
        val windows = alertDialog.window
        windows.let {
            windows?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            windows?.setGravity(Gravity.CENTER)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog?.show()


    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}