package com.example.kotlin_shopping_shop.fragments.LoginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kotlin_shopping_shop.R
import com.example.kotlin_shopping_shop.activities.ShoppingActivity
import com.example.kotlin_shopping_shop.databinding.FragmentLoginBinding
import com.example.kotlin_shopping_shop.utils.RegisterValidation
import com.example.kotlin_shopping_shop.utils.Resource
import com.example.kotlin_shopping_shop.viewModel.LoginViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onNavigateToRegister()
        onLogin()
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
                        binding.btnLogin.background =
                            resources.getDrawable(R.drawable.blue_background)

                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        binding.btnLogin.revertAnimation()
                        binding.btnLogin.background =
                            resources.getDrawable(R.drawable.blue_background)
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