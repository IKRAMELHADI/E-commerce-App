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
import com.example.ecomapp.activities.ShoppingActivity
import com.example.ecomapp.databinding.FragmentIntroBinding
import com.example.ecomapp.viewModel.IntroductionViewModel
import com.example.ecomapp.viewModel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.ecomapp.viewModel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_intro) {
    private lateinit var binding: FragmentIntroBinding

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

        binding.startButton.setOnClickListener {
            // Navigation directe sans ViewModel
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }



    }
}
