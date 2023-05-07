package com.example.apartmentbuddy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.ComplainPersistence

class ComplainHome : Fragment() {
    private val complains  = ComplainPersistence()
    private val user_id : String = "dhruv@gmail.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val views= inflater.inflate(R.layout.fragment_complain_home, container, false)
        val back : Button = views.findViewById(R.id.complain_back)

        back.setOnClickListener {
            views.findNavController().navigate(R.id.action_global_home2)
        }
        val viewComplains: CardView = views.findViewById(R.id.ViewComplain)
        viewComplains.setOnClickListener{
            viewComplains.findNavController().navigate(ComplainHomeDirections.actionFragmentComplainHomeToComplainListView())
        }

        val new_complain: CardView = views.findViewById(R.id.newComplain)

        new_complain.setOnClickListener {
            views.findNavController().navigate(com.example.apartmentbuddy.fragments.ComplainHomeDirections.actionFragmentComplainHomeToFragmentComplainForm())
        }
        return views
    }
}