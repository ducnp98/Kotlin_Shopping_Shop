package com.example.kotlin_shopping_shop.fragments.Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlin_shopping_shop.adapters.HomeViewPageAdapter
import com.example.kotlin_shopping_shop.databinding.FragmentHomeBinding
import com.example.kotlin_shopping_shop.fragments.Categories.AccessoryCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.BaseCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.ChairCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.CupboardCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.FurnitureCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.MainCategoryFragment
import com.example.kotlin_shopping_shop.fragments.Categories.TableCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairCategoryFragment(),
            CupboardCategoryFragment(),
            AccessoryCategoryFragment(),
            FurnitureCategoryFragment(),
            BaseCategoryFragment(),
            TableCategoryFragment(),
        )

        val viewPager2Adapter =
            HomeViewPageAdapter(categoriesFragment, childFragmentManager, this.lifecycle)
        binding.viewpager.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            when(position) {
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Accessory"
                4 -> tab.text = "Furniture"
                5 -> tab.text = "Base"
                6 -> tab.text = "Table"
            }
        }.attach()
    }
}