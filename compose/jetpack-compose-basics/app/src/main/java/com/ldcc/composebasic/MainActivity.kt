package com.ldcc.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    MyApp()
                }
            }
        }
    }
}

// 5. 계층이 깊어질수록 가독성이 떨어질 수 있다. 재사용이 가능한 UI를 최대한 작게 나누면서 읽기 쉽게하기 위해
// MyApp 메소드를 생성하여 관리하여 가독성을 해결할 수 있다.
@Composable
private fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Greeting("Android")
    }
}

// 3. @Composable : Compose Function임을 알려주고 있는 어노테이션
// 4. Surface를 통하여 Text의 배경색상을 변경할 수 있다.
// 4. Text를 보면 자동으로 White 색상으로 변경되는 것을 확인할 수 있다. -> Material Component에서 지원해주는 기능
// 4. UI 요소들은 modifier 파라미터를 가지고 있으며, Modifier는 UI의 배치방식들을 설명하고 있다.
@Composable
fun Greeting(name: String) {
    Surface(color = MaterialTheme.colors.primary) {
        Text(text = "Hello $name!", modifier = Modifier.padding(24.dp))
    }
}

// 3. Preview 어노테이션의 경우, 미리보기 화면을 제공한다.
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeBasicTheme {
        MyApp()
    }
}