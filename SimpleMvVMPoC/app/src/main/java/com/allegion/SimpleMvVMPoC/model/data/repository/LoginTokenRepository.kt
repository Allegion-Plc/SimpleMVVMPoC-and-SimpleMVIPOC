package com.allegion.SimpleMvVMPoC.model.data.repository

import com.allegion.SimpleMvVMPoC.model.data.AuthCredential

interface LoginTokenRepository {

    /**
     Perform login against a remote datasource and return a string token
     representing access to a logged-into resource
     @param: username, part of a login credential combination representing a user
     @param: password, part of a login credential combination verifying a user own a credential pair
     @returns: AuthCredentials, a token representing access to a logged-into resource
     **/
    suspend fun login(username: String, password: String): AuthCredential
}
