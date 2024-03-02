package com.example.arduino1test

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import com.amplifyframework.AmplifyException
import com.example.arduino1test.ui.theme.Arduino1TestTheme
import com.amplifyframework.core.Amplify
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import com.amplifyframework.pushnotifications.pinpoint.AWSPinpointPushNotificationsPlugin
import com.example.arduino1test.R.string.msg_token_fmt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging
import org.junit.runner.manipulation.Ordering
import kotlin.math.log

class MainActivity : ComponentActivity() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize AWS Amplify
        configureAmplify()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        Log.d("TAG", "This is a debug message");

        setContent {
            Arduino1TestTheme {
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
            }
            Column {// Upload File
                Button(onClick = { uploadFile() }) {
                    Text("Start")
                }
                // Download File
                Button(onClick = { downloadFile() }) {
                    Text("Download File")
                }

            }

        }
    }
    private fun configureAmplify() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.addPlugin(AWSPinpointPushNotificationsPlugin())
            Amplify.configure(applicationContext)
            Log.i("kilo", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("kilo", "Could not initialize Amplify")
        }
        Log.i("kilo", "AWS Configuration Done")
    }
    private fun uploadFile() {
        val exampleFile = File(applicationContext.filesDir, "ExampleKey.txt")
        exampleFile.writeText("Example file contents")

        Amplify.Storage.uploadFile("ExampleKey.txt", exampleFile,
            {Log.i("MyAmplifyApp", "Successfully Uploaded")},
            {Log.e("MyAmplifyApp", "Upload Failed", it)}
        )
    }

    private fun downloadFile() {
        val file = File("${applicationContext.filesDir}/ExampleKey.txt")
        Amplify.Storage.downloadFile("ExampleKey", file,
            { Log.i("MyAmplifyApp", "Successfully downloaded: ${it.file.name}") },
            { Log.e("MyAmplifyApp",  "Download Failure", it) }
        )
        try {
            val reader = BufferedReader(FileReader(file))
            var line: String?

            while (true) {
                line = reader.readLine()
                if (line == null) break

                // Log each line to the Logcat
                Log.d("MyAmplifyApp", line)
            }

            reader.close()
        } catch (e: Exception) {
            Log.e("MyAmplifyApp", "Error reading file: ${e.message}", e)
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Arduino1TestTheme {
        Greeting("Android")
    }
}