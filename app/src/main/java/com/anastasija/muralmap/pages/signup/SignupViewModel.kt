package com.anastasija.muralmap.pages.signup

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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

class SignupViewModel {
    var uiState by mutableStateOf(SignupUiState())
        private set

    fun onPhotoCaptured(uri: Uri) {
        uiState = uiState.copy(
            photoUri = uri,
            isPhotoSelected = true
        )
    }

    fun uploadPicture(context: Context, onResult: (String?) -> Unit) {
        val photoUri = uiState.photoUri ?: return onResult(null)

        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, photoUri)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val cloudName = "du2p6m04l"
        val uploadPreset = "MuralMap" // TODO: replace
        val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
        
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "profile.jpg",
                data.toRequestBody("image/jpeg".toMediaTypeOrNull()))
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        onResult(null)
                        return
                    }
                    val json = JSONObject(it.body?.string() ?: "")
                    val imageUrl = json.getString("secure_url")
                    onResult(imageUrl)
                }
            }
        })
    }
}