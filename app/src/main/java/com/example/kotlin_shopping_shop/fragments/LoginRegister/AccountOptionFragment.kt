package com.example.kotlin_shopping_shop.fragments.LoginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlin_shopping_shop.databinding.FragmentAccountOptionBinding
import com.example.kotlin_shopping_shop.databinding.FragmentIntroductionBinding

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
}