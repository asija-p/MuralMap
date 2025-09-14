package com.anastasija.muralmap.pages.signup

import android.net.Uri

data class SignupUiState(

    val photoUri: Uri? = null,
    val isPhotoSelected: Boolean = false,
)