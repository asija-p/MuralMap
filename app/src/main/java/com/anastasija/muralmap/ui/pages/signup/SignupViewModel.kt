package com.anastasija.muralmap.ui.pages.signup

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anastasija.muralmap.domain.usecase.UploadProfileImageUseCase
import com.anastasija.muralmap.ui.auth.AuthViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
class SignupViewModel(
    private val uploadProfileImageUseCase: UploadProfileImageUseCase = UploadProfileImageUseCase()
) : ViewModel() {

    var uiState by mutableStateOf(SignupUiState())
        private set

    fun onEmailChanged(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun onFullNameChanged(value: String) {
        uiState = uiState.copy(fullName = value)
    }

    fun onPhoneChanged(value: String) {
        uiState = uiState.copy(phoneNumber = value)
    }

    fun onPasswordChanged(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun onPhotoCaptured(uri: Uri) {
        uiState = uiState.copy(
            photoUri = uri,
            isPhotoSelected = true
        )
    }

    fun signup(context: Context, authViewModel: AuthViewModel) {
        val state = uiState
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val imageUrl = state.photoUri?.let { uploadProfileImageUseCase(context, it) }
            if (imageUrl != null) {
                authViewModel.signup(
                    state.email.trim(),
                    state.password.trim(),
                    state.fullName.trim(),
                    state.phoneNumber.trim(),
                    imageUrl
                )
            } else {
                uiState = uiState.copy(isLoading = false, errorMessage = "Image upload failed")
            }
        }
    }
}