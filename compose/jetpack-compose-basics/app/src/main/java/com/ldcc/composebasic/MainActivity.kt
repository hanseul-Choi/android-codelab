package com.ldcc.composebasic

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
//    Column(modifier = Modifier.padding(vertical = 4.dp)) {
//        for (name in names) {
//            Greeting(name = name)
//        }
//    }

    // 10. remember 객체는 그저 recomposition에 대해서만 data를 저장할 뿐, Activity가 재시작하면 data가 날라간다.
    // 따라서, rememberSaveable을 통해 configuration change에 대응할 수 있다.
    // mutableStateOf : 객체 변화 이벤트를 위해, remember : recomposition에 대응하기 위해, rememberSaveable : recomposition과 Configure Change에 대응하기 위해
//    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    // 8. Composable은 Visible처리 메소드가 따로 존재하지 않아, 메소드의 호출여부로 UI Tree를 수정하여 UI elements의 Visible 처리를 진행한다.
    if(shouldShowOnboarding) {
        // 8. OnboardingScreen에서의 event를 받아 shouldShowOnBoarding을 처리하기 위해 콜백 진행
        OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
    } else {
        Greetings()
    }
}

// 9. LazyColumn을 이용하여 RecyclerView와 같은 효과를 낸다.
// 9. LazyColumn은 RecyclerView와 다르게 View를 Recycle 하지않고 생성하는데, 그 이유는 컴포즈의 생성비용이 저렴하기 때문이다.
@Composable
private fun Greetings(names: List<String> = List(1000){ "$it" } ) {
//    Column(modifier = Modifier.padding(vertical = 4.dp)) {
//        for(name in names) {
//            Greeting(name = name)
//        }
//    }


    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
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
//    val expanded = remember { mutableStateOf(false) }
//
//    // 7. remember를 쓸 필요가 없다. 어차피 expanded가 기억되고 있기 때문
//    // 11. animateDpAsState를 이용하여 컴포즈 애니메이션 적용이 가능하다.
//    // 11. animationSpec을 통해서 애니메이션 커스텀이 가능하다.
//    val extraPadding by animateDpAsState(
//        if(expanded.value) 48.dp else 0.dp,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        )
//    )
//
//    Surface(
//        color = MaterialTheme.colors.primary,
//        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
//    ) {
//        Row(modifier = Modifier.padding(24.dp)) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(bottom = extraPadding.coerceAtLeast(0.dp)) // 11. spring animation 처리시 만약 음수까지 가게된다면 padding error 발생!
//            ) {
//                Text(text = "Hello, ")
//                // 12. Text style 적용, 또한 copy를 이용해서 기존 style에 새로운 프로퍼티 추가
//                Text(text = name, style = MaterialTheme.typography.h4.copy(
//                    fontWeight = FontWeight.ExtraBold
//                    )
//                )
//            }
//
//            OutlinedButton(onClick = { expanded.value = !expanded.value }) {
//                Text(if(expanded.value) "Show less" else "Show more")
//            }
//        }
//    }
    // 13. Card Compose elements 이용
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
    }
}

// 13. String resource와 IconButton 사용
@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(text = "Hello, ")
            Text(
                text = name,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if(expanded) {
                Text(text = ("Composem ipsum color sit lazy, " +
                        "padding theme elit, sed do bouncy. ").repeat(4))
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if(expanded) {
                    stringResource(id = R.string.show_less)
                } else {
                    stringResource(id = R.string.show_more)
                }
            )
        }
    }
}

// 7. state 정보는 가장 최상단의 부모에 위치하는 것이 좋다. 이를 state hoisting이라고 하며, 버그 예방과 재사용성을 높이고 테스트 용이성을 높인다.
@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit) {
//    // 8. by 키워드를 통해 value property없이 값을 수정할 수 있다.
//    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Surface {
        Column( // 8. Column의 배치 속성으로 가운데로 배치가 가능하다.
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            IconButton(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }
}

// 8. Preview는 여러 개 표시가 가능하다.
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    ComposeBasicTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

// 3. Preview 어노테이션의 경우, 미리보기 화면을 제공한다.
// 6. Preview 화면의 크기를 지정할 수 있다.
// 12. uiMode를 통해 Dark Mode를 테스트할 수 있다.
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun DefaultPreview() {
    ComposeBasicTheme {
        Greetings()
    }
}