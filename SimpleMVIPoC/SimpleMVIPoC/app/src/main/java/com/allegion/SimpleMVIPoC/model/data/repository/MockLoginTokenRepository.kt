package com.allegion.SimpleMVIPoC.model.data.repository

import com.allegion.SimpleMVIPoC.model.data.AuthCredential
import kotlinx.coroutines.delay

class MockLoginTokenRepository : LoginTokenRepository {
    companion object {
        // A mocked auth token in the form of a JWT
        // for more information on JWT's see https://jwt.io/
        const val MOCK_AUTH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG5fRXZlcnltYW4iLCJpYXQiOjE1MTYyMzkwMjJ9.RXDQgCgiI56ZipxWipBMT8VXPO83IgIgF2DRhMW4UbE"

        // A value for delaying this mock repositories execution in milliseconds to simulate
        // network delay
        const val MOCK_EXECUTION_DELAY = 1000L
    }

    /**
     * This mocked repository always succeeds regardless of login combination
     * and should be used for demonstration purposes only
     */
    override suspend fun login(username: String, password: String): AuthCredential {
        delay(MOCK_EXECUTION_DELAY)
        return AuthCredential(authToken = MOCK_AUTH_TOKEN)
    }
}
