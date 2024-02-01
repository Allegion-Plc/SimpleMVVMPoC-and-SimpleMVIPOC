package com.allegion.SimpleMVIPoC.model.data.repository

import com.allegion.SimpleMVIPoC.model.data.AuthCredential
import com.allegion.SimpleMVIPoC.model.data.Permission

interface PermissionsRepository {

    /**
     Request a String list of permissions from a remote datasource for a given logged-in user
     @param: authCredential, a credential retrieved by logging a user into a remote resource
     @returns: List<Permission>, a list of Permission objects that the user account is granted
     **/
    suspend fun retrievePermissions(authCredential: AuthCredential): List<Permission>
}
