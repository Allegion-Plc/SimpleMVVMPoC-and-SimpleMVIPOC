package com.allegion.SimpleMvVMPoC.model.data.repository

import com.allegion.SimpleMvVMPoC.model.data.AuthCredential
import com.allegion.SimpleMvVMPoC.model.data.Permission
import kotlinx.coroutines.delay

class MockPermissionsRepository : PermissionsRepository {
    companion object {
        // A mocked list of permissions to simulate a real user permissions list
        val PERMISSION_LIST = arrayListOf(Permission.CREATE_USER, Permission.DELETE_USER)

        // A value for delaying this mock repositories execution in milliseconds to simulate
        // network delay
        const val MOCK_EXECUTION_DELAY = 1000L
    }

    /**
     * This mocked repository always returns a defined list of permissions
     * and should be used for demonstration purposes only
     */
    override suspend fun retrievePermissions(authCredential: AuthCredential): List<Permission> {
        delay(MOCK_EXECUTION_DELAY)
        return PERMISSION_LIST
    }
}
