package com.example.kotlin_shopping_shop.fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kotlin_shopping_shop.R
import com.example.kotlin_shopping_shop.databinding.FragmentAccountOptionBinding

class AccountOptionFragment : Fragment() {
    lateinit var binding: FragmentAccountOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountOptionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigate()
    }

    private fun navigate() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionFragment_to_registerFragment)
        }
    }
}