package com.example.ecomapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.data.Address
import com.example.ecomapp.databinding.FragmentAddressBinding
import com.example.ecomapp.util.Resource
import com.example.ecomapp.util.hideBottomNavigationView
import com.example.ecomapp.viewModel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()

    private var address: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Récupération via arguments si besoin (optionnel, ou à retirer complètement)
        address = arguments?.getParcelable("address")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()

        if (address == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(address!!.addressTitle)
                edFullName.setText(address!!.fullName)
                edStreet.setText(address!!.street)
                edPhone.setText(address!!.phone)
                edCity.setText(address!!.city)
                edState.setText(address!!.state)
            }
        }

        binding.buttonSave.setOnClickListener {
            val addressTitle = binding.edAddressTitle.text.toString()
            val fullName = binding.edFullName.text.toString()
            val street = binding.edStreet.text.toString()
            val phone = binding.edPhone.text.toString()
            val city = binding.edCity.text.toString()
            val state = binding.edState.text.toString()

            val newAddress = Address(addressTitle, fullName, street, phone, city, state)
            viewModel.addAddress(newAddress)
        }

        observeAddAddressResult()
        observeValidationErrors()
    }

    private fun observeAddAddressResult() {
        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeValidationErrors() {
        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
