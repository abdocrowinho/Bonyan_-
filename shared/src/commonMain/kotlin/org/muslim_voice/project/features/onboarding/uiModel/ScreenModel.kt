package org.muslim_voice.project.features.onboarding.uiModel

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.muslim_voice.project.generated.resources.Res
import org.muslim_voice.project.generated.resources.ic_calls
import org.muslim_voice.project.generated.resources.onboarding_azan
import org.muslim_voice.project.generated.resources.onboarding_building
import org.muslim_voice.project.generated.resources.onboarding_library

data class ScreenModel(
    val iconTittle: DrawableResource ,
    val title : String,
    val subTitle : String ,
    val color : Color
)

val onBoardingScreens = listOf(
    ScreenModel(
        Res.drawable.onboarding_building,
                  title = "بنُيان",
        subTitle="هو تطبيق يهدف إلي مشاركة الأصحاب ان ليس \nوحدهم من يحاول البقاء على طاعة الله  ",
        color=Color.White
    ),
    ScreenModel(
        iconTittle = Res.drawable.onboarding_azan ,
        title = "الأذان",
        subTitle="يمكنك أن تؤذن في أصدقائك في توقيت الصلوات",
        color=Color.White
    ),
    ScreenModel(
        iconTittle = Res.drawable.onboarding_library ,
        title = "المكتبة",
        subTitle="يقرأ كلا الاصدقاء القرأن ويستطيع كل افراد \nالمجموعة ان يتطلعوا على مصاحف الأخرين ",
        color=Color.White
    ),
    ScreenModel(  iconTittle = Res.drawable.onboarding_library ,
        title = "الذكر",
        subTitle="يمكنك أن تذكر اصدقاء دائما بذكر الله من \n خلال اللاسلكي حتى تأنث بذكرهم ويأنثوا بذكرك",
        color=Color.White),
    ScreenModel(  iconTittle = Res.drawable.ic_calls ,
        title = "الهدف",
        subTitle="حتى نتشارك طاعة كما تشاركنا كل شيئ وتذكر دائما \n <المسلم والمسلم كالبنيان يشد بعضهم بعض> ",
        color=Color.White),

    )
