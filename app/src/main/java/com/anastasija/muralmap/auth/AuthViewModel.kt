package com.anastasija.muralmap.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anastasija.muralmap.auth.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if(auth.currentUser==null) {
            _authState.value = AuthState.Unauthenticated
        }
        else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()) {
            _authState.value=AuthState.Error("Email or password can't be empty")
            return

        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful) {
                    _authState.value=AuthState.Authenticated
                }
                else {
                    _authState.value=AuthState.Error(task.exception?.message?:"Something went wrong")
                }

            }
    }

    fun signup(email: String, password: String, fullName: String, phoneNumber: String, profileImageUrl: String?) {

        if(email.isEmpty() || password.isEmpty()) {
            _authState.value=AuthState.Error("Email or password can't be empty")
            return

        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    val uid = user?.uid

                    if (uid != null) {
                        val db = Firebase.firestore;

                        val userMap = hashMapOf(
                            "uid" to uid,
                            "email" to email,
                            "fullName" to fullName,
                            "phoneNumber" to phoneNumber,
                            "profileImageUrl" to profileImageUrl
                        )

                        db.collection("users")
                            .document(uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error("Firestore error: ${e.message}")
                            }
                    } else {
                        _authState.value = AuthState.Error("No UID found")
                    }
                }
                else {
                    _authState.value=AuthState.Error(task.exception?.message?:"Something went wrong")
                }

            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}
