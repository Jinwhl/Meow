package com.example.meow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.meow.ui.theme.MeowTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeowTheme {
                MainScreen()
            }
        }
    }
}

/*TODO : 코드 가독성 개선 필요!!!!!!!!!!!!!! */

@Composable
fun MainScreen() {
    class CatImage(
        val species: String,    // 고양이 종
        val name: String,       // 고양이 이름
        val imageId: Int,       // 이미지 경로 아이디
    )

    // imageList 에 이미지 클래스 누적
    val imageList = listOf(
        CatImage("Siamese Cat", "정이", R.drawable.test_cat_siam),
        CatImage("La Perm Cat", "세이", R.drawable.test_cat_laperm),
        CatImage("Felis Cat", "영이", R.drawable.test_cat_felis),
        CatImage("Soft Bread", "식빵", R.drawable.test_cat_bread),
    )
    val imageIndex = remember { mutableIntStateOf(0) } // index:<Int> 라서 mutable`Int`StateOf 사용

    var blurFlag by remember { mutableStateOf(false) }
    val defaultModifier = Modifier
        .height(200.dp)
        .width(280.dp)
    val blurModifier = Modifier
        .height(200.dp)
        .width(280.dp)
        .blur(
            radiusX = 10.dp,
            radiusY = 10.dp,
            edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
        )
    val modifierSetting =
        if (blurFlag) {
            blurModifier
        }
        else { defaultModifier }

    var grayFlag by remember { mutableStateOf(false) }
    val colorSetting =
        if (grayFlag) {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        }
        else { null }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        val currentImage = imageList[imageIndex.value] // imageIndex.value : <Int>
        Spacer(modifier = Modifier.height(16.dp))
        // Section 1 : 이미지 좌우로 조회
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        if (imageIndex.value > 0) {
                            imageIndex.value = (imageIndex.value - 1) % imageList.size
                        } else {
                            imageIndex.value = imageList.size - 1
                        }
                    }
                ) {
                    Text(text = "<")
                }
                Spacer(modifier = Modifier.weight(1f)) // 왼쪽 버튼과 이미지 사이의 빈 공간
                Image(
                    painter = painterResource(id = imageList[imageIndex.value].imageId),
                    contentDescription = imageList[imageIndex.value].species,
                    contentScale = ContentScale.Fit,
                    modifier = modifierSetting,
                    colorFilter = colorSetting,
                )
                Spacer(modifier = Modifier.weight(1f)) // 이미지와 오른쪽 버튼 사이의 빈 공간
                TextButton(
                    onClick = {
                        imageIndex.value = (imageIndex.value + 1) % imageList.size
                    }
                ) {
                    Text(text = ">")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Section 2 : Image Shaking Button
        var isRunning by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = isRunning) {
            while (isRunning) {
                delay(500) // Import : Long~
                imageIndex.value = (imageIndex.value + 1) % imageList.size
            }
        }
        Button(onClick = {
            isRunning = !isRunning
            if (isRunning) {
                imageIndex.value = 0
            }
        }) {
            Text(if (isRunning) "Stop" else "Shake")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Section 3 : 그냥 이미지 정보
        Text(text = "Species : " + currentImage.species)
        Text(text = "Name : " + currentImage.name)
        Spacer(modifier = Modifier.height(32.dp))
        Divider()

        // Section 4 : 이미지 필터 적용
        Row{
            Button(onClick = {
                grayFlag = !grayFlag // 흑백과 컬러 전환
            }) {
                Text(text = if (grayFlag) "Color" else "Black") // 버튼 텍스트 설정
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                blurFlag = !blurFlag // 흑백과 컬러 전환
            }) {
                Text(text = if (blurFlag) "Original" else "Blur") // 버튼 텍스트 설정
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}