package com.anastasija.muralmap.ui.pages.signup

import android.net.Uri

data class SignupUiState(
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val photoUri: Uri? = null,
    val isPhotoSelected: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)