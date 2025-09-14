package com.anastasija.muralmap.pages.signup

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SignupViewModel {
    var uiState by mutableStateOf(SignupUiState())
        private set

    fun onPhotoCaptured(uri: Uri) {
        uiState = uiState.copy(
            photoUri = uri,
            isPhotoSelected = true
        )
    }
}