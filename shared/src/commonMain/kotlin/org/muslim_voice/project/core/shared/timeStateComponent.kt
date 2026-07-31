//package com.dodeal.features.shared
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.dodeal.features.dashboard.ui.components.getTimeColor
//import com.dodeal.features.shared.texts.CustomText
//import org.muslim_voice.project.core.utilities.sdp
//import org.muslim_voice.project.core.utilities.ssp
//
//@Composable
//fun TimeStateContainer(time: String){
//    val meetingColorState= getTimeColor(time)
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(20.sdp)).height(20.sdp).
//            border(width = .5.sdp, color = meetingColorState, shape = RoundedCornerShape(20.sdp))
//            .background(meetingColorState.copy(alpha = 0.15f))
//            .padding(horizontal = 10.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = time,
//            color = meetingColorState,
//            fontSize = 11.ssp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}