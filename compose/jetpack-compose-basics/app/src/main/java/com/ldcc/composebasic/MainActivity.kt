package com.ldcc.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ldcc.composebasic.ui.theme.ComposeBasicTheme

// 3. Activity는 AndroidApp의 EntryPoint이다. setContent를 통해 layout을 정의한다.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 3. ComposeBasicTheme는 안드로이드의 스타일 테마를 정의한다.
            ComposeBasicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

// 3. @Composable : Compose Function임을 알려주고 있는 어노테이션
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

// 3. Preview 어노테이션의 경우, 미리보기 화면을 제공한다.
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeBasicTheme {
        Greeting("Android")
    }
}