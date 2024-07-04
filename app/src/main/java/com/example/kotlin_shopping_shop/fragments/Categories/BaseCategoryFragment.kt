package com.example.kotlin_shopping_shop.fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlin_shopping_shop.databinding.FragmentBaseCategoryBinding

class BaseCategoryFragment : Fragment() {
    lateinit var binding: FragmentBaseCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}