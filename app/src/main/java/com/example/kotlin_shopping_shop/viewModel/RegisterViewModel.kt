package com.example.kotlin_shopping_shop.viewModel

import androidx.lifecycle.ViewModel
import com.example.kotlin_shopping_shop.data.User
import com.example.kotlin_shopping_shop.di.AppModule
import com.example.kotlin_shopping_shop.utils.Constants.USER_COLLECTION
import com.example.kotlin_shopping_shop.utils.RegisterFieldState
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.utils.validateEmail
import com.example.kotlin_shopping_shop.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register


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
                        saveUserInfo(user, it.uid)
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

    private fun saveUserInfo(user: User, userUid: String) {
        fireStore.collection(USER_COLLECTION).document(userUid).set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message)
            }
    }
}