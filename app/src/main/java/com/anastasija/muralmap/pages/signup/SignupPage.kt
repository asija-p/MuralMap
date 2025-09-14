package com.anastasija.muralmap.pages.signup

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.anastasija.muralmap.auth.AuthState
import com.anastasija.muralmap.auth.AuthViewModel
import java.io.File

@Composable
fun SignupPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val signupViewModel = remember { SignupViewModel() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { signupViewModel.onPhotoCaptured(it) }
    }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let { signupViewModel.onPhotoCaptured(it) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted → launch camera
            val photoFile = File.createTempFile("profile_", ".jpg", context.cacheDir).apply {
                deleteOnExit()
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    
    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Signup Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    val photoFile = File.createTempFile("profile_", ".jpg", context.cacheDir).apply {
                        deleteOnExit()
                    }
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        photoFile
                    )
                    cameraImageUri = uri
                    cameraLauncher.launch(uri)
                } else {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }) {
                Text("Take a photo")
            }

            Spacer(Modifier.width(16.dp))

            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Pick a picture")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Selected Picture")

        signupViewModel.uiState.photoUri?.let { uri ->
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape),
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop
            )
        }

        OutlinedTextField(
            value= email,
            onValueChange = { email = it },
            label = { Text(text="Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,


        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value= fullName,
            onValueChange = { fullName = it },
            label = { Text(text="Full name") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            singleLine = true,


            )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value= phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(text="Phone") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            singleLine = true,


            )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value= password,
            onValueChange = { password = it },
            label = { Text(text="Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine=true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }

        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            email.trim()
            password.trim()
            authViewModel.signup(email, password, fullName, phoneNumber)
        }, enabled = authState.value!=AuthState.Loading
        ) {
            Text(text= "Create account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text="Already have an account, Login")
        }
    }
}