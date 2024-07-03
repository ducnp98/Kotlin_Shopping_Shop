package com.example.kotlin_shopping_shop.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kotlin_shopping_shop.data.User
import com.example.kotlin_shopping_shop.di.AppModule
import com.example.kotlin_shopping_shop.utils.RegisterFieldState
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.utils.validateEmail
import com.example.kotlin_shopping_shop.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register


    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()

    fun createUserWithEmailAndPassword(user: User, password: String) {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)


        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        if (shouldRegister) {
            runBlocking {
                _register.emit(Resource.Loading())
            }

            AppModule.provideFirebaseAuth().createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->
                    it.user?.let {
                        _register.value = Resource.Success(it)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        } else {
            val registerFieldState = RegisterFieldState(emailValidation, passwordValidation)
            runBlocking {
                _validation.send(registerFieldState)
            }
        }

    }
}