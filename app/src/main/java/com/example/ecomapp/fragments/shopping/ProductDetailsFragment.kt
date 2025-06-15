package com.example.ecomapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.adapters.ColorsAdapter
import com.example.ecomapp.adapters.SizesAdapter
import com.example.ecomapp.adapters.ViewPager2Images
import com.example.ecomapp.data.CartProduct
import com.example.ecomapp.data.Product
import com.example.ecomapp.util.Resource
import com.example.ecomapp.util.hideBottomNavigationView
import com.example.ecomapp.viewModel.DetailsViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var product: Product
    private val viewPagerAdapter by lazy { ViewPager2Images() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    private lateinit var imageClose: ImageView
    private lateinit var buttonAddToCart: MaterialButton
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductDescription: TextView
    private lateinit var tvProductColors: TextView
    private lateinit var tvProductSize: TextView
    private lateinit var viewPagerProductImages: androidx.viewpager2.widget.ViewPager2
    private lateinit var rvColors: RecyclerView
    private lateinit var rvSizes: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)

        imageClose = view.findViewById(R.id.imageClose)
        buttonAddToCart = view.findViewById(R.id.buttonAddToCart)
        tvProductName = view.findViewById(R.id.tvProductName)
        tvProductPrice = view.findViewById(R.id.tvProductPrice)
        tvProductDescription = view.findViewById(R.id.tvProductDescription)
        tvProductColors = view.findViewById(R.id.tvProductColors)
        tvProductSize = view.findViewById(R.id.tvProductSize)
        viewPagerProductImages = view.findViewById(R.id.viewPagerProductImages)
        rvColors = view.findViewById(R.id.rvColors)
        rvSizes = view.findViewById(R.id.rv_sizes)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            product = bundle.getParcelable("product")
                ?: throw IllegalArgumentException("Product is missing in arguments")
        } else {
            throw IllegalArgumentException("Arguments are null")
        }

        setupSizesRv()
        setupColorsRv()
        setupViewpager()

        imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        buttonAddToCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {

                        buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }

                    is Resource.Error -> {

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        tvProductName.text = product.name
        tvProductPrice.text = "$ ${product.price}"
        tvProductDescription.text = product.description

        if (product.colors.isNullOrEmpty())
            tvProductColors.visibility = View.INVISIBLE
        if (product.sizes.isNullOrEmpty())
            tvProductSize.visibility = View.INVISIBLE

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let { colorsAdapter.differ.submitList(it) }
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
    }

    private fun setupViewpager() {
        viewPagerProductImages.adapter = viewPagerAdapter
    }

    private fun setupColorsRv() {
        rvColors.apply {
            adapter = colorsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRv() {
        rvSizes.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
