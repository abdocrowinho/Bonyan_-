# تشغيل مشروع صوت المسلم (Windows)

## الطريقة ١ — من Android Studio / IntelliJ (الأسهل)

1. افتح المجلد: `c:\Users\Abdo\IdeaProjects\muslimVoice`
2. انتظر حتى ينتهي **Gradle Sync** (شريط التقدم تحت).
3. من القائمة العلوية اختر **Run configuration**:
   - **desktopApp** → تشغيل على الكمبيوتر (Windows)
   - **androidApp** → تشغيل على موبايل أو Emulator
4. اضغط زر ▶ **Run**.

> لو مش شايف `desktopApp`: من Gradle (يمين) → `muslimVoice` → `desktopApp` → `Tasks` → `compose desktop` → `run` (دبل كليك).

---

## الطريقة ٢ — من Terminal داخل المشروع

```powershell
cd c:\Users\Abdo\IdeaProjects\muslimVoice
.\gradlew :desktopApp:run
```

أول مرة ممكن تاخد 5–10 دقائق (تحميل Gradle ومكتبات).

---

## لو ظهر `DefaultArtifactPublicationSet`

معناه **Gradle 9** شغال مع **AGP 8.x** — مش متوافقين. تأكد إن `gradle/libs.versions.toml` فيه `agp = "9.0.1"` و Kotlin `2.3.21` (تم ضبطهم في المشروع).

بعدها:
```powershell
.\gradlew --stop
.\gradlew clean
.\gradlew :desktopApp:run
```

في Android Studio: **File → Sync Project with Gradle Files**.

---

## لو ظهر خطأ Gradle / checksum

1. احذف مجلد Gradle التالف (اختياري):
   - `%USERPROFILE%\.gradle\wrapper\dists`
2. شغّل تاني:
   ```powershell
   .\gradlew :desktopApp:run --no-daemon
   ```

تم إصلاح `gradle-wrapper.properties` لاستخدام **Gradle 9.1.0** مع الـ checksum الصحيح.

**مهم:** إصدارات المشروع لازم تكون متوافقة مع بعض:
- Gradle **9.1.0**
- Android Gradle Plugin **9.0.1**
- Kotlin **2.3.21** + Compose **1.11.0**

لو غيّرت AGP لـ 8.x مع Gradle 9 → هيظهر خطأ `DefaultArtifactPublicationSet`.

---

## متطلبات

| للتشغيل | المطلوب |
|---------|---------|
| Desktop | JDK 17 أو 21 (يفضل 21) |
| Android | JDK + Android SDK + جهاز أو Emulator |

تحقق من Java:
```powershell
java -version
```

---

## ماذا تتوقع بعد التشغيل؟

1. شاشة ترحيب **صوت المسلم**
2. تسجيل دخول أو إنشاء حساب (تجريبي — أي إيميل وكلمة ٦ أحرف)
3. التطبيق الرئيسي: تبويبات **المسجد | القرآن | الحديث**
