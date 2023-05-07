package com.example.apartmentbuddy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.databinding.FragmentPostAdvertisementOptionsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PostAdvertisementOptions.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostAdvertisementOptions : Fragment() {
    private lateinit var binding: FragmentPostAdvertisementOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostAdvertisementOptionsBinding.inflate(layoutInflater)
        return binding.root
    }
}