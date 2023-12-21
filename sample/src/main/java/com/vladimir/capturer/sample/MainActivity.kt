package com.vladimir.capturer.sample

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.vladimir.capturer.api.CapturerCollector
import com.vladimir.capturer.sample.compose.CapturerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val client by lazy {
        createOkHttpClient(applicationContext)
    }

    private val httpTasks by lazy {
        listOf(BulkTask(client))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CapturerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column {
                        Button(onClick = {
                            for (task in httpTasks) {
                                task.run()
                            }
                        }) {
                            Text(text = "Test the calls")
                        }
                        Button(onClick = {
                            generateExportFile()
                        }) {
                            Text(text = "Export to Txt file")
                        }
                    }
                }
            }
        }
    }
    private fun generateExportFile() {
        lifecycleScope.launch {
            val uri =
                withContext(Dispatchers.IO) {
                    CapturerCollector(this@MainActivity)
                        .writeTransactions(this@MainActivity, null)
                }
            if (uri == null) {
                Toast.makeText(applicationContext, R.string.export_to_file_failure, Toast.LENGTH_SHORT).show()
            } else {
                val successMessage = applicationContext.getString(R.string.export_to_file_success, uri.path)
                Toast.makeText(applicationContext, successMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CapturerTheme {
        Greeting("Android")
    }
}
