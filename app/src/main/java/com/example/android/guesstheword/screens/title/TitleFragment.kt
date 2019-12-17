package com.example.android.guesstheword.screens.title


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.android.guesstheword.R
import kotlinx.android.synthetic.main.fragment_title.*

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        play_game_button.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToGame())
        }
    }


}
