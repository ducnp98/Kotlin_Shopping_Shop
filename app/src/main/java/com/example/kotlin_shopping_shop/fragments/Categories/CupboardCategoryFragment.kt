package com.example.kotlin_shopping_shop.fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlin_shopping_shop.databinding.FragmentCupboardCategoryBinding

class CupboardCategoryFragment : Fragment() {
    lateinit var binding: FragmentCupboardCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCupboardCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}