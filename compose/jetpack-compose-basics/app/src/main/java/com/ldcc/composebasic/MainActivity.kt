package com.ldcc.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
// 6. Composable function도 Kotlin처럼 사용할 수 있다. (ex. for문 적용)
@Composable
private fun MyApp(names: List<String> = listOf("World", "Compose")) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

// 3. @Composable : Compose Function임을 알려주고 있는 어노테이션
// 4. Surface를 통하여 Text의 배경색상을 변경할 수 있다.
// 4. Text를 보면 자동으로 White 색상으로 변경되는 것을 확인할 수 있다. -> Material Component에서 지원해주는 기능
// 4. UI 요소들은 modifier 파라미터를 가지고 있으며, Modifier는 UI의 배치방식들을 설명하고 있다.
// 6. Column을 통해 레이아웃을 세로로 배치할 수 있다.
// 6. Modifier에서 vertical과 horizontal로 부분적인 padding을 적용할 수 있다.
// 7. State관련 data를 Composable function안에서 관리하면 원하는대로 동작하지 않을 수 있다.
// 7. recomposition은 Composable data의 변경을 감지하여 다시 Composable function을 부르는 것이다.
// 7. 즉, State관련 data를 Composable function안에서 초기화하면 recomposition이 발생해 다시 초기화하여 데이터가 변하지 않는다.
// 7. 이를 해결하기위해, remember 객체(실제 내부에는 cache를 이용)를 통해 recomposition에도 데이터가 초기화되지 않게 한다.
@Composable
fun Greeting(name: String) {
    val expanded = remember { mutableStateOf(false) }

    // 7. remember를 쓸 필요가 없다. 어차피 expanded가 기억되고 있기 때문
    val extraPadding = if(expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
            ) {
                Text(text = "Hello, ")
                Text(text = name)
            }

            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if(expanded.value) "Show less" else "Show more")
            }
        }
    }
}

// 3. Preview 어노테이션의 경우, 미리보기 화면을 제공한다.
// 6. Preview 화면의 크기를 지정할 수 있다.
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    ComposeBasicTheme {
        MyApp()
    }
}