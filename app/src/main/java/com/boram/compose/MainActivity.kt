package com.boram.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boram.compose.data.sample.SampleData
import com.boram.compose.ui.theme.ComposeTheme
import com.boram.compose.data.Message

/**
 * 이미지와 텍스트가 포함된 확장형 및 애니메이션 메시지의 목록을 효율적으로 표시하는 간단한 채팅 화면
    - 구성 가능한 함수 정의
    - 컴포저블에 다른 요소 추가
    - 레이아웃 컴포저블을 사용하여 UI 구성요소 구조화
    - 수정자를 사용한 컴포저블 확장
    - 효율적인 목록 만들기
    - 상태 추적 유지 및 수정
    - 컴포저블에 사용자 상호작용 추가
    - 메시지 확장 중 메시지에 애니메이션 적용
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                //1. 텍스트뷰
                //ComposeTheme {
                //    MessageCard(Message("Android", "Jetpack Compose"))
                //}

                //2. 리스트
                ComposeTheme() {
                    Conversation(messages = SampleData.conversationSample)
                }
            }
        }
    }
}

//1. 텍스트
@Composable
fun MessageCard(msg: Message) {

    //행
    Row(modifier = Modifier.padding(all = 8.dp)) { //행 패딩 추가
        Image(
            painter = painterResource(id = R.drawable.svg_ic_account_avatar_profile_user_icon),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape) //이미지 circle 모양으로 변경
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        //이미지와 아래 열 사이의 공간
        Spacer(modifier = Modifier.width(8.dp))

        //펼침 체크용 - remember를 사용하여 메모리에 로컬 상태를 저장하고 mutableStateOf에 전달된 값의 변경사항을 추적
        var isExpanded by remember { mutableStateOf(false) }

        //점진적으로 변경
        val surfaceColor: Color by animateColorAsState(
            targetValue = if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )

        //열사용
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.title,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)

            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }

        }
    }
}

//1-1. 텍스트 미리보기
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    //ui.theme/Theme.kt 에 있는 기본 정의된 테마
    ComposeTheme() {
        MessageCard(
            msg = Message("Colleague","Hey, take a look at Jetpack Compose, it's great!")
        )
    }
}

//2. 목록형
@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { msg ->
            MessageCard(msg = msg)
        }
    }
}

//2. 목록형 미리 보기
@Preview
@Composable
fun PreviewConversation() {
    ComposeTheme() {
        Conversation(messages = SampleData.conversationSample)
    }
}