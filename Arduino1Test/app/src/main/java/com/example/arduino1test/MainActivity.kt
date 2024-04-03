package com.example.arduino1test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.amplifyframework.pushnotifications.pinpoint.AWSPinpointPushNotificationsPlugin
import com.example.arduino1test.ui.theme.Arduino1TestTheme
import android.util.Log
import org.json.JSONObject
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAmplify()
        setContent {
            Arduino1TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InputAndSendUI()
                }
            }
        }
    }

    @Composable
    fun InputAndSendUI() {
        var text by remember { mutableStateOf("") }

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter Label") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if(text.isNotEmpty()) {
                        uploadLabelData(text)
                        text = "" // Reset text field after sending
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Send")
            }

            // Add a Stop Button
            Button(
                onClick = { createAndUploadStopFile() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Stop")
            }
            Button(
                onClick = { createAndUploadContinueFile() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Continue")
            }
        }
    }

    private fun createAndUploadContinueFile() {
        val content = "Continue"
        val filename = "state.txt"
        val file = File(cacheDir, filename).apply {
            writeText(content)
        }

        val key = "txt/$filename"
        Amplify.Storage.uploadFile(
            key,
            file,
            { result -> Log.i("Upload", "Successfully uploaded: ${result.key}") },
            { error -> Log.e("Upload", "Upload failed", error) }
        )
    }

    private fun uploadLabelData(label: String) {
        val jsonObject = JSONObject().apply {
            put("label", label)
        }
        uploadJsonToS3(label, jsonObject)
    }

    private fun uploadJsonToS3(label: String, jsonObject: JSONObject) {
        val jsonString = jsonObject.toString()
        val filename = "labels.json"
        val file = File(cacheDir, filename).also {
            it.writeText(jsonString)
        }

        val key = "json/$filename"
        Amplify.Storage.uploadFile(
            key,
            file,
            { result -> Log.i("Upload", "Successfully uploaded: ${result.key}") },
            { error -> Log.e("Upload", "Upload failed", error) }
        )
    }

    private fun createAndUploadStopFile() {
        val content = "stop"
        val filename = "state.txt"
        val file = File(cacheDir, filename).apply {
            writeText(content)
        }

        val key = "txt/$filename"
        Amplify.Storage.uploadFile(
            key,
            file,
            { result -> Log.i("Upload", "Successfully uploaded: ${result.key}") },
            { error -> Log.e("Upload", "Upload failed", error) }
        )
    }

    private fun configureAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSPinpointPushNotificationsPlugin())
            Amplify.configure(applicationContext)
            Log.i("MainActivity", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("MainActivity", "Could not initialize Amplify", error)
        }
    }
}