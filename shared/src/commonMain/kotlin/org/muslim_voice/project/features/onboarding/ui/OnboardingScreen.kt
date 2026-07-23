package org.muslim_voice.project.features.onboarding.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.muslim_voice.project.core.component.AppButton
import org.muslim_voice.project.core.ui.components.AppBackground
import org.muslim_voice.project.features.onboarding.effect.OnboardingEffect
import org.muslim_voice.project.features.onboarding.intent.OnboardingIntent
import org.muslim_voice.project.features.onboarding.uiModel.onBoardingScreens
import org.muslim_voice.project.features.onboarding.viewModel.OnboardingViewModel
import org.muslim_voice.project.navigation.AppNavigator
import org.muslim_voice.project.navigation.Screens
import kotlin.math.absoluteValue

// Fixed palette — page-specific "data.color" from the model is no longer used for text.
private val TitleColor = Color(0xFF0D7E5E)
private val SubtitleColor = Color(0xFF6B7280)
private val IconGradientStart = Color(0xFF0D7E5E)
private val IconGradientEnd = Color(0xFF15A578)
private val ActiveDotColor = Color(0xFF0D7E5E)
private val InactiveDotColor = Color(0xFFE0E0E0)
private val SkipTextColor = Color(0xFF9CA3AF)

@Composable
fun OnboardingScreen(
    navigator: AppNavigator,
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { onBoardingScreens.size })

    LaunchedEffect(state.currentPage) {
        pagerState.animateScrollToPage(
            page = state.currentPage,
            animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            viewModel.onIntent(OnboardingIntent.SetPage(pagerState.currentPage))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                OnboardingEffect.NavigateToLogin ->
                    navigator.navigateToOuter(
                        Screens.SelectLan,
                        popUpTo = Screens.Onboarding,
                        inclusive = true
                    )
            }
        }
    }

    val isLastPage = pagerState.currentPage == onBoardingScreens.size - 1

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) { page ->
                    val data = onBoardingScreens[page]

                    // Distance of this page from the current scroll position, used to
                    // drive a soft fade + scale as pages settle into place.
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                            ).absoluteValue.coerceIn(0f, 1f)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                            .graphicsLayer {
                                alpha = 1f - pageOffset
                                val scale = 1f - (pageOffset * 0.15f)
                                scaleX = scale
                                scaleY = scale
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PulsingIconBadge {
                            Image(
                                painter = painterResource(data.iconTittle),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TitleColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = data.subTitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = SubtitleColor
                        )
                    }
                }

                PagerDotsIndicator(
                    pageCount = onBoardingScreens.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppButton(
                        onClick = {
                            viewModel.onIntent(OnboardingIntent.Next)
                        },
                        text = if (isLastPage) "ابدأ" else "التالي"
                    )

                    TextButton(
                        onClick = {
                            viewModel.onIntent(
                                OnboardingIntent.SetPage(onBoardingScreens.size - 1)
                            )
                        }
                    ) {
                        Text(
                            text = "تخطي",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SkipTextColor
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun PulsingIconBadge(content: @Composable () -> Unit) {
    val transition = rememberInfiniteTransition(label = "iconPulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconPulseScale"
    )

    Box(
        modifier = Modifier
            .size(92.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(IconGradientStart, IconGradientEnd),
                    start = Offset(0f, 0f),
                    end = Offset(1f, 1f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Pill-shaped progress dots: the active dot smoothly morphs from a small
 * circle into a wide pill instead of just swapping a color.
 */
@Composable
private fun PagerDotsIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage

            val width by animateDpAsState(
                targetValue = if (isActive) 24.dp else 8.dp,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                label = "dotWidth"
            )
            val color by androidx.compose.animation.animateColorAsState(
                targetValue = if (isActive) ActiveDotColor else InactiveDotColor,
                animationSpec = tween(durationMillis = 300),
                label = "dotColor"
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}