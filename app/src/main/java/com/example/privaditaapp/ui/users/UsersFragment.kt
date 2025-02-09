package com.example.privaditaapp.ui.users

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.privaditaapp.R
import com.example.privaditaapp.databinding.FragmentUsersBinding
import com.example.privaditaapp.ui.adapters.PlayersAdapter
import com.example.privaditaapp.ui.firebase.FirebaseManager
import com.example.privaditaapp.ui.models.Players
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseManager: FirebaseManager
    private lateinit var playersAdapter: PlayersAdapter

    //ui elements
    private lateinit var addButton : FloatingActionButton

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

        addButton = binding.root.findViewById(R.id.addUserButton)
        addButton.setOnClickListener {
            showAddPlayerDialog()
        }

        fetchPlayers()

        registerForContextMenu(binding.recyclerView)

        Log.d("UsersFragment", "onCreateView: UsersFragment view created")

        return root
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = requireActivity().menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val player = playersAdapter.currentList[info.position]
                deletePlayer(player)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun deletePlayer(player: Players) {
        firebaseManager.deletePlayer(player.name, {
            Toast.makeText(context, "Player deleted successfully", Toast.LENGTH_SHORT).show()
            fetchPlayers() // Refresh the list
        }, { exception ->
            Toast.makeText(context, "Error deleting player: ${exception.message}", Toast.LENGTH_SHORT).show()
        })
    }

    private fun showAddPlayerDialog() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add New Player")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val playerName = input.text.toString().trim()
            val regex = Regex("^[a-zA-Z0-9]+$")
            if (playerName.isNotEmpty() && regex.matches(playerName)) {
                val newPlayer = Players(name = playerName, elo = 1000) // Example player data
                firebaseManager.addPlayer(newPlayer, playerName, {
                    Toast.makeText(context, "Player added successfully", Toast.LENGTH_SHORT).show()
                    fetchPlayers() // Refresh the list
                }, { exception ->
                    Toast.makeText(context, "Error adding player: ${exception.message}", Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(context, "Player name must contain only letters and numbers", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
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