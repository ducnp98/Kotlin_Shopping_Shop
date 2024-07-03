package com.example.kotlin_shopping_shop.fragments.LoginRegister

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kotlin_shopping_shop.R
import com.example.kotlin_shopping_shop.activities.ShoppingActivity
import com.example.kotlin_shopping_shop.databinding.FragmentLoginBinding
import com.example.kotlin_shopping_shop.dialogs.setupBottomDialog
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val loginMvvm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onNavigateToRegister()
        onLogin()
        onForgetPassword()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onForgetPassword() {
        binding.txtForgetPass.setOnClickListener {
            setupBottomDialog {
                loginMvvm.resetEmail(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            loginMvvm.resetEmail.collect() {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Reset link was send to your email ${it.data}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error ${it.message}", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    is Resource.Unspecified -> Unit
                }
            }
        }
    }

    private fun onNavigateToRegister() {
        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun onLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            loginMvvm.loginWithEmailAndPassword(email, password)
        }
        lifecycleScope.launchWhenStarted {
            loginMvvm.login.collect() {
                when (it) {
                    is Resource.Loading -> binding.btnLogin.startAnimation()
                    is Resource.Success -> {
                        binding.btnLogin.revertAnimation()

                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        binding.btnLogin.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            loginMvvm.validation.collect() {
                if (it.email is RegisterValidation.Failed) {
                    binding.etEmail.error = it.email.message
                    binding.etEmail.requestFocus()
                }
                if (it.password is RegisterValidation.Failed) {
                    binding.etPassword.error = it.password.message
                    binding.etEmail.requestFocus()
                }
            }
        }
    }
}