package com.example.redlibros_albpabvic.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class loginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    fun onLoginChange(newMail:String){
        _email.value = newMail
        validCredentials()
    }

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled


    private fun validCredentials() {
        _isLoginEnabled.value = Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }
}