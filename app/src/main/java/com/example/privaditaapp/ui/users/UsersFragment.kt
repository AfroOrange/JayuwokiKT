package com.example.privaditaapp.ui.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.privaditaapp.R
import com.example.privaditaapp.databinding.FragmentUsersBinding
import com.example.privaditaapp.ui.adapters.PlayersAdapter
import com.example.privaditaapp.ui.firebase.FirebaseManager
import com.example.privaditaapp.ui.models.Players

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var playersAdapter: PlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseManager = FirebaseManager()
        playersAdapter = PlayersAdapter { player -> navigateToPlayerDetail(player) }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playersAdapter
        }

        fetchPlayers()

        Log.d("UsersFragment", "onCreateView: UsersFragment view created")

        return root
    }

    private fun fetchPlayers() {
        firebaseManager.fetchPlayers({ players ->
            playersAdapter.submitList(players)
        }, { exception ->
            Toast.makeText(context, "Error fetching players: ${exception.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun navigateToPlayerDetail(player: Players) {
        val bundle = Bundle().apply {
            putParcelable("player", player)
        }
        findNavController().navigate(R.id.action_usersFragment_to_playerDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}