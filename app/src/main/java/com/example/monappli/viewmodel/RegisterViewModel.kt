package com.example.monappli.viewmodel
import androidx.lifecycle.ViewModel
import com.example.monappli.data.User
import com.example.monappli.util.Ressource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel(){

    private val _register= MutableStateFlow<Ressource<FirebaseUser>>(Ressource.Loading())
    val register : Flow<Ressource<FirebaseUser>> = _register
    fun createAccountWithEmail(user :User,password:String){
        firebaseAuth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {
                    it.user?.let{
                        _register.value=Ressource.Success(it)
                    }
            }.addOnFailureListener{
                _register.value=Ressource.Error(it.message.toString())
            }
    }




}