package com.allegion.SimpleMVIPoC.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.allegion.SimpleMVIPoC.intent.MainIntent
import com.allegion.SimpleMVIPoC.model.data.repository.MockLoginTokenRepository
import com.allegion.SimpleMVIPoC.model.data.repository.MockPermissionsRepository
import com.allegion.SimpleMVIPoC.model.state.MainState
import com.allegion.SimpleMVIPoC.model.theme.SimpleMVIPoCTheme
import com.allegion.SimpleMVIPoC.viewmodel.MainActivityViewModel
import com.allegion.SimpleMVIPoC.R

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainActivityViewModel(
            loginRepository = MockLoginTokenRepository(),
            permissionRepository = MockPermissionsRepository()
        )

        // Set up our observations
        viewModel.mainState.observe(
            this,
            Observer<MainState>() {
                setPageContent()
            }
        )

        setPageContent()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun setPageContent() {
        setContent {
            SimpleMVIPoCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            var username by remember { mutableStateOf("") }
                            var password by remember { mutableStateOf("") }

                            val displayLoginScreen =
                                viewModel.mainState.value == null || (viewModel.mainState.value == MainState.IDLE && viewModel.retrievedPermissions.value.isNullOrEmpty())
                            val displaySpinner =
                                viewModel.mainState.value == MainState.RETRIEVING_PERMISSIONS
                            val displayPermissionsList =
                                viewModel.mainState.value == MainState.IDLE && !viewModel.retrievedPermissions.value.isNullOrEmpty()
                            val shouldDisplayErrorToast =
                                viewModel.mainState.value == MainState.FAILED
                            if (displayLoginScreen) {
                                TextField(
                                    label = { Text(text = getString(R.string.username_field_text)) },
                                    value = username,
                                    onValueChange = { newText ->
                                        username = newText
                                    }
                                )

                                TextField(
                                    label = { Text(text = getString(R.string.password_field_text)) },
                                    value = password,
                                    onValueChange = { newText ->
                                        password = newText
                                    }
                                )

                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Blue,
                                        contentColor = Color.White
                                    ),
                                    onClick = {
                                        viewModel.handleIntent(
                                            MainIntent.RetrievePermissionIntent(
                                                username = username,
                                                password = password
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .height(86.dp)
                                        .width(200.dp)
                                        .padding(6.dp),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(getString(R.string.login_button_text))
                                }
                            } else if (displaySpinner) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(50.dp) // Set the size of the spinner
                                        .padding(16.dp) // Add padding if needed
                                )
                            } else if (displayPermissionsList) {
                                Column {
                                    viewModel.retrievedPermissions.value!!.forEach { message ->
                                        Text(message.toString())
                                        Spacer(modifier = Modifier.height(1.dp))
                                    }
                                }
                            }
                            if(shouldDisplayErrorToast){
                                toast.makeText("error occurred")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleButton(label: String, onClick: () -> Unit) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleMVIPoCTheme {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(
            label = { Text(text = "Username") },
            value = username,
            onValueChange = { newText ->
                username = newText
            }
        )

        TextField(
            label = { Text(text = "Password") },
            value = password,
            onValueChange = { newText ->
                password = newText
            }
        )
        SimpleButton(
            label = "Login",
            onClick = {
            }
        )
    }
}
