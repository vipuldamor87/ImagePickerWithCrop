package com.vipuldamor87.libimagecrop

import android.net.Uri


interface OnImagePickedListener {
    fun onImagePicked(imageUri: Uri?)
}