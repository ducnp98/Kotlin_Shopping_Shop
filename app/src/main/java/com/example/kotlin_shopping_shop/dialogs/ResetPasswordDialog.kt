package com.example.kotlin_shopping_shop.dialogs

import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.kotlin_shopping_shop.R
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

@RequiresApi(Build.VERSION_CODES.S)
fun Fragment.setupBottomDialog(onSendClick: (String) -> Unit) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password, null)
    dialog.setContentView(view)

    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

    dialog.window!!.setBackgroundDrawableResource(R.drawable.gray_transparent_background)

    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.et_reset_email)
    val btnCancel = view.findViewById<CircularProgressButton>(R.id.btn_cancel_reset)
    val btnSend = view.findViewById<CircularProgressButton>(R.id.btn_send_reset)

    btnSend.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
}