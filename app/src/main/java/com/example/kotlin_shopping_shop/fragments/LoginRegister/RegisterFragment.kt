package com.example.kotlin_shopping_shop.fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kotlin_shopping_shop.R
import com.example.kotlin_shopping_shop.data.User
import com.example.kotlin_shopping_shop.databinding.FragmentRegisterBinding
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onRegister()
        onNavigateToLogin()
    }

    private fun onNavigateToLogin() {
        binding.txtLoginSuggest.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun onRegister() {
        binding.btnRegister.setOnClickListener {
            val user = User(
                binding.etFirstName.text.toString().trim(),
                binding.etLastName.text.toString().trim(),
                binding.etEmail.text.toString().trim(),
                ""
            )
            val password = binding.etPassword.text.toString().trim()
            viewModel.createUserWithEmailAndPassword(user, password)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect() {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.btnRegister.revertAnimation()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                    }

                    is Resource.Error -> {
                        binding.btnRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect() {
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }
                if (it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.etPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }

        }
    }
}