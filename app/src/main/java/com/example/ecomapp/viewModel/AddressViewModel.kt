package com.example.ecomapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecomapp.data.Address
import com.example.ecomapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)

        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }
            val docRef = firestore.collection("user")
                .document(auth.uid!!)
                .collection("address")
                .document()

            val addressWithId = address.copy(docRef.id)

            docRef.set(addressWithId).addOnSuccessListener {
                viewModelScope.launch { _addNewAddress.emit(Resource.Success(addressWithId)) }
            }.addOnFailureListener {
                viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
            }
        } else {
            viewModelScope.launch {
                _error.emit("All fields are required")
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            firestore.collection("user")
                .document(auth.uid!!)
                .collection("address")
                .document(addressId)
                .delete()
                .addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Success(Address()))
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.state.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty()
    }
}
