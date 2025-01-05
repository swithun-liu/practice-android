package screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import cmpblog.composeapp.generated.resources.Res
import cmpblog.composeapp.generated.resources.*
import component.Background
import theme.primary
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomePage() {
    var isShowSocialDialog by remember { mutableStateOf(false) }
    var socialType by remember { mutableStateOf(SocialType.QQ) }
    BoxWithConstraints {
        val maxW = maxWidth
        val min = minOf(maxWidth, maxHeight)
        val dynamicHeight = min * 2f / 3f
        val dynamicWidth = dynamicHeight * 16f / 9f

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Background()

            if (maxW < 800.dp) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.rem_icon),
                        contentDescription = null,
                        modifier = Modifier.weight(1f)
                    )

                    UserInfo(
                        modifier = Modifier.weight(2f).fillMaxHeight().padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        onClick = { action ->
                            actionEvent(action) {
                                if (socialType == SocialType.QQ || socialType == SocialType.WECHAT) {
                                    socialType = it
                                    isShowSocialDialog = true
                                }
                            }
                        }
                    )
                }
            } else {
                Row(
                    modifier = Modifier.width(dynamicWidth).height(dynamicHeight)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Color.White.copy(alpha = 0.5f)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    UserInfo(
                        modifier = Modifier.weight(2f).fillMaxHeight().padding(horizontal = 20.dp),
                        onClick = { action ->
                            actionEvent(action) {
                                if (socialType == SocialType.QQ || socialType == SocialType.WECHAT) {
                                    socialType = it
                                    isShowSocialDialog = true
                                }
                            }
                        }
                    )
                    Image(
                        painter = painterResource(Res.drawable.rem_icon),
                        contentDescription = null,
                        modifier = Modifier.weight(1f)
                    )
                }
            }



            AnimatedVisibility(visible = isShowSocialDialog) {
                Dialog(onDismissRequest = {
                    isShowSocialDialog = false
                }) {
                    Card(
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.rem_icon), contentDescription = null,
                            modifier = Modifier.sizeIn(maxHeight = 400.dp)
                        )
                    }
                }
            }
        }
    }

}

private fun actionEvent(action: Action, callback: (socialType: SocialType) -> Unit) {
    when (action) {
        is Action.SocialAction -> {
            when (action.socialType) {
                SocialType.EMAIL,
                SocialType.BLOG,
//                SocialType.GITHUB -> window.open(action.socialType.url)
                SocialType.GITHUB -> {
                }

                SocialType.BILI -> {}
                SocialType.QQ,
                SocialType.WECHAT -> callback(action.socialType)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onClick: (action: Action) -> Unit
) {
    val skills = remember {
        listOf("Kotlin", "Android", "Flutter", "Compose for multiplatform", "Java", "Ktor")
    }
    val socialLinks = remember {
        listOf(
            Res.drawable.icon_blog,
            Res.drawable.icon_github,
            Res.drawable.icon_bili,
            Res.drawable.icon_qq,
            Res.drawable.icon_wechat
        )
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(text = "Lao Hei", style = MaterialTheme.typography.h3, fontWeight = FontWeight.Bold)

        Column {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                skills.fastForEach {
                    Chip(
                        onClick = {},
                        enabled = false,
                        colors = ChipDefaults.chipColors(
                            disabledBackgroundColor = Color.White,
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(text = it)
                    }
                }
            }
            Box(
                modifier = Modifier.width(120.dp).height(13.dp).padding(top = 5.dp).clip(CircleShape)
                    .background(primary)
            )
        }

        Text(text = "Compose, Kotlin, Android, Kotlin Multiplatform, Ktor 开发者")

        Button(
            onClick = {
                onClick(Action.SocialAction(SocialType.EMAIL))
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(imageVector = Icons.Default.Email, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "发送邮件")
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            socialLinks.fastForEach {
                Button(
                    onClick = {
                        val socialType = when (it) {
                            Res.drawable.icon_blog -> SocialType.BLOG
                            Res.drawable.icon_github -> SocialType.GITHUB
                            Res.drawable.icon_bili -> SocialType.BILI
                            Res.drawable.icon_qq -> SocialType.QQ
                            Res.drawable.icon_wechat -> SocialType.WECHAT
                            else -> SocialType.GITHUB
                        }
                        onClick(Action.SocialAction(socialType))
                    },
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Icon(painter = painterResource(it), contentDescription = null, tint = primary)
                }
            }
        }
    }
}