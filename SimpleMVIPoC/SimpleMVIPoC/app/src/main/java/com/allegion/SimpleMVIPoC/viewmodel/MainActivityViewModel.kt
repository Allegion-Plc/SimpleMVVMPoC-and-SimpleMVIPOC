package com.allegion.SimpleMVIPoC.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.allegion.SimpleMVIPoC.intent.MainIntent
import com.allegion.SimpleMVIPoC.model.data.Permission
import com.allegion.SimpleMVIPoC.model.data.repository.LoginTokenRepository
import com.allegion.SimpleMVIPoC.model.data.repository.PermissionsRepository
import com.allegion.SimpleMVIPoC.model.state.MainState
import com.allegion.SimpleMVIPoC.model.state.RetrievePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val loginRepository: LoginTokenRepository,
    private val permissionRepository: PermissionsRepository
) : ViewModel() {
    companion object {
        // CoroutineScope tied to a default collection of threads with no specialization
        val corutineScope = CoroutineScope(Dispatchers.Default)
    }

    // A flow is like livedata, except it can consume updates over time and continuously notify
    // subscribers of those updates. We use this to get updates about the state of
    // retrievePermissions over time
    private val _retrievePermissionsState = MutableStateFlow<RetrievePermissionsState>(RetrievePermissionsState.IDLE)

    // The UI collects from this StateFlow to get its state updates
    private val retrievePermissionsState: StateFlow<RetrievePermissionsState> = _retrievePermissionsState

    /****/

    private val _mainState = MutableLiveData<MainState>()

    // Expose a state to any who would wish to know it, such that they may
    // do not have to calculate it on their own
    val mainState: LiveData<MainState>
        get() = _mainState

    /****/

    var permissionStateObservationThread: Job? = null

    // A private mutable resource is established such that no other classes
    // can modify our viewmodels data
    private val _retrievedPermissions = MutableLiveData<List<Permission>>()

    // the non-mutable equivalent of our livedata references the mutable
    // value upon request
    val retrievedPermissions: LiveData<List<Permission>>
        get() = _retrievedPermissions

    fun handleIntent(intent: MainIntent) {
        if (intent is MainIntent.RetrievePermissionIntent) {
            permissionStateObservationThread = CoroutineScope(Dispatchers.Main).launch {
                // This collect method beings active monitoring of the permissionState
                // and will react to any change in the state for the lifecycle of this viewModel
                // from this point forward
                retrievePermissionsState.collect { currentRetrievePermissionsState ->
                    when (currentRetrievePermissionsState) {
                        RetrievePermissionsState.IDLE -> {
                            _mainState.postValue(MainState.IDLE)
                        }
                        RetrievePermissionsState.PENDING -> {
                            _mainState.postValue(MainState.RETRIEVING_PERMISSIONS)
                        }
                        RetrievePermissionsState.COMPLETE -> {
                            _mainState.postValue(MainState.IDLE)
                            // once weve completed, cancel ourselves
                            permissionStateObservationThread?.cancel()
                        }

                        else -> {
                            _mainState.postValue(MainState.IDLE)
                        }
                    }
                }
            }
            retrievePermissions(
                username = intent.username,
                password = intent.password,
                resultListener = _retrievedPermissions,
                stateListener = _retrievePermissionsState
            )
        } else {
            throw Exception()
        }
    }

    private fun retrievePermissions(
        username: String,
        password: String,
        stateListener: MutableStateFlow<RetrievePermissionsState>,
        resultListener: MutableLiveData<List<Permission>>
    ) {
        corutineScope.launch {
            // Emit to our stateflow that we are busy
            stateListener.emit(RetrievePermissionsState.PENDING)
            val retrievedAuthCredential = loginRepository.login(
                username = username,
                password = password
            )

            val retrievedPermissions = permissionRepository.retrievePermissions(
                authCredential = retrievedAuthCredential
            )

            resultListener.postValue(retrievedPermissions)
            // Emit to our stateflow that we are complete
            stateListener.emit(RetrievePermissionsState.IDLE)
        }
    }
}
