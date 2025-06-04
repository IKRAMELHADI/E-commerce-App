package com.example.ecomapp.fragments.LoginRegister



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.activities.shopping
import com.example.ecomapp.databinding.FragmentIntroBinding
import com.example.ecomapp.viewModel.IntroductionViewModel
import com.example.ecomapp.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.ecomapp.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect {
                when (it) {
                    SHOPPING_ACTIVITY -> {
                        Intent(requireActivity(), shopping::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    ACCOUNT_OPTIONS_FRAGMENT -> {
                        findNavController().navigate(it)
                    }

                    else -> Unit
                }
            }
        }

        binding.startButton.setOnClickListener {
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }
}