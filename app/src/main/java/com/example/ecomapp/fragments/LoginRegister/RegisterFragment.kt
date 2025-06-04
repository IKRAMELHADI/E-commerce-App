package com.example.ecomapp.fragments.LoginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.data.User
import com.example.ecomapp.databinding.FragmentRegisterBinding
import com.example.ecomapp.util.RegisterValidation
import com.example.ecomapp.util.Resource
import com.example.ecomapp.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.HaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            RegisterButton.setOnClickListener {
                val user = User(
                    firstname.text.toString().trim(),
                    lastname.text.toString().trim(),
                    Email.text.toString().trim()
                )
                val password = password.text.toString()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.RegisterButton.apply {
                            text = "Chargement..."
                            isEnabled = false
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
                        }
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.RegisterButton.apply {
                            text = "Inscription réussie"
                            isEnabled = true
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_gray700))
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.RegisterButton.apply {
                            text = "Réessayer"
                            isEnabled = true
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_red))

                }

            } else -> Unit
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect { validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.Email.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.password.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
        }
    }
}
