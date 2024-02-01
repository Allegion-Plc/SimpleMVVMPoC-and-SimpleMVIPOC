package com.allegion.SimpleMvVMPoC.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allegion.SimpleMvVMPoC.model.data.AuthCredential
import com.allegion.SimpleMvVMPoC.model.data.Permission
import com.allegion.SimpleMvVMPoC.model.data.repository.LoginTokenRepository
import com.allegion.SimpleMvVMPoC.model.data.repository.PermissionsRepository
import com.allegion.SimpleMvVMPoC.model.state.MainState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val loginRepository: LoginTokenRepository,
    private val permissionRepository: PermissionsRepository
) : ViewModel() {
    companion object {
        // CoroutineScope tied to a default collection of threads with no specialization
        val corutineScope = CoroutineScope(Dispatchers.Default)
    }

    // A private mutable resource is established such that no other classes
    // can modify our viewmodels data
    private val _retrievedPermissions = MutableLiveData<List<Permission>>()

    // the non-mutable equivalent of our livedata references the mutable
    // value upon request
    val retrievedPermissions: LiveData<List<Permission>>
        get() = _retrievedPermissions

    private val _mainState = MutableLiveData<MainState>()

    // Expose a state to any who would wish to know it, such that they may
    // do not have to calculate it on their own
    val mainState: LiveData<MainState>
        get() = _mainState

    fun login(username: String, password: String) {
        // Note that odd check for idle here, as this method has to have knowledge that
        // it begins the flow of the application
        if (mainState.value == null || mainState.value == MainState.IDLE) {
            corutineScope.launch {
                _mainState.postValue(MainState.AUTHENTICATING)
                val retrievedAuthCredential = loginRepository.login(
                    username = username,
                    password = password
                )
                retrievePermissions(retrievedAuthCredential)
            }
        }
    }

    private fun retrievePermissions(authCredential: AuthCredential) {
        if (mainState.value != null && mainState.value == MainState.AUTHENTICATING) {
            _mainState.postValue(MainState.RETRIEVING_PERMISSIONS)
            corutineScope.launch {
                val retrievedPermissions = permissionRepository.retrievePermissions(
                    authCredential = authCredential
                )
                _retrievedPermissions.postValue(retrievedPermissions)
                _mainState.postValue(MainState.IDLE)
            }
        }
    }
}
