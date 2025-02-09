package com.example.privaditaapp.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.privaditaapp.databinding.FragmentPlayerDetailBinding
import com.example.privaditaapp.ui.models.Players

class PlayerDetailFragment : Fragment() {

    private var _binding: FragmentPlayerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val player = arguments?.getParcelable<Players>("player")
        player?.let {
            binding.playerName.text = it.name
            binding.playerElo.text = it.elo.toString()
            binding.playerWins.text = it.wins.toString()
            binding.playerLosses.text = it.losses.toString()
            // Set other player details
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}