package com.example.kotlin_shopping_shop.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_shopping_shop.utils.RegisterFieldState
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.utils.validateEmail
import com.example.kotlin_shopping_shop.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun loginWithEmailAndPassword(email: String, password: String) {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldLogin =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        if (shouldLogin) {
            runBlocking { _login.emit(Resource.Loading()) }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message))
                    }
                }
        } else {
            val registerFieldState = RegisterFieldState(emailValidation, passwordValidation)
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }
}