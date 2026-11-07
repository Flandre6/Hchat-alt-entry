import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
}

android {
    namespace = "h.Hchat"
    compileSdk = 37

    val autoVersionCode = System.getenv("HCAT_VERSION_CODE")?.toIntOrNull() ?: 1
    val autoVersionName = System.getenv("HCAT_VERSION_NAME")?.takeIf { it.isNotBlank() } ?: "1.0.0"

    defaultConfig {
        applicationId = "h.Hchat"
        minSdk = 27
        targetSdk = 37
        versionCode = autoVersionCode
        versionName = autoVersionName

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
    }

    signingConfigs {
        create("。。") {
            storeFile = file("keystore/。。.jks")
            storePassword = providers.environmentVariable("HCAT_STORE_PASSWORD").orNull
                ?: error("缺少 HCAT_STORE_PASSWORD")
            keyAlias = providers.environmentVariable("HCAT_KEY_ALIAS").orNull
                ?: error("缺少 HCAT_KEY_ALIAS")
            keyPassword = providers.environmentVariable("HCAT_KEY_PASSWORD").orNull
                ?: error("缺少 HCAT_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("。。")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        jniLibs {
            // 保持 true：proot 需要 libproot.so 被解压到 nativeLibraryDir 才能 exec 运行。
            // libtermux.so 的加载不走 classloader.findLibrary，改由 Hchat 用绝对路径
            // System.load() 加载（见 NativeLibraryLoader.loadTermux + com.termux.terminal.JNI 补丁）。
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "DebugProbesKt.bin"
            excludes += "META-INF/*.version"
            excludes += "META-INF/com/android/build/gradle/app-metadata.properties"
            excludes += "META-INF/version-control-info.textproto"
            excludes += "META-INF/**/LICENSE"
            excludes += "META-INF/**/LICENSE.txt"
            excludes += "META-INF/**/NOTICE"
            excludes += "META-INF/**/NOTICE.txt"
            excludes += "kotlin/**"
            excludes += "kotlin-tooling-metadata.json"
            excludes += "frameworks/android/*.apk"
            excludes += "android/attrs.xml"
            excludes += "android/attrs_manifest.xml"
            excludes += "android/res-map.txt"
            excludes += "clst/core.jcst"
            excludes += "export/**"
            excludes += "jadx/core/deobf/conditions/tlds.txt"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.17")
    implementation("io.github.billywei01:fastkv:3.0.1")
    implementation(files("libs/dexkit-2.2.0-76551eb-idle.aar"))
    implementation("com.google.flatbuffers:flatbuffers-java:23.5.26")
    implementation("dev.rikka.ndk.thirdparty:cxx:1.2.0")
    implementation("com.github.REAndroid:ARSCLib:V1.3.8")
    implementation("io.github.skylot:jadx-dex-input:1.5.5") {
        exclude(group = "com.google.guava", module = "guava")
    }
    implementation("com.google.guava:guava:33.5.0-android")
    implementation("com.android.tools.smali:smali-baksmali:3.0.9")
    implementation("com.highcapable.kavaref:kavaref-core:1.1.0")
    implementation("com.jakewharton.android.repackaged:dalvik-dx:16.0.1")
    implementation("com.linkedin.dexmaker:dexmaker:2.28.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.61.android8")
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.ui)
    implementation("androidx.compose.material3:material3:1.5.0-alpha26")
    implementation("androidx.lifecycle:lifecycle-runtime:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.savedstate:savedstate:1.2.1")
    implementation("androidx.navigationevent:navigationevent:1.1.2")
    implementation("androidx.navigationevent:navigationevent-compose:1.1.2")
    implementation(files("libs/miuix-ui-android-0.9.4.aar"))
    implementation(files("libs/miuix-core-android-0.9.4.aar"))
    implementation(files("libs/miuix-squircle-android-0.9.4.aar"))
    implementation(files("libs/miuix-preference-android-0.9.4.aar"))
    implementation(files("libs/miuix-icons-android-0.9.4.aar"))
    implementation(files("libs/miuix-nav-android-0.9.4.aar"))
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation(files("libs/miuix-blur-android-0.9.4.aar"))
    implementation(files("libs/miuix-shader-android-0.9.4.aar"))
    // Termux 终端（GPLv3，LSPilot 同款）：terminal-view 依赖 terminal-emulator
    implementation(files("libs/terminal-emulator-0.118.0.aar"))
    implementation(files("libs/terminal-view-0.118.0.aar"))
}

// 构建完成后自动复制 APK 到 dist/
tasks.register<Copy>("copyToDist") {
    from(layout.buildDirectory.file("outputs/apk/release/app-release.apk"))
    into(rootProject.layout.projectDirectory.dir("dist"))
    rename { providers.environmentVariable("HCAT_APK_NAME").orElse("Hchat-release-signed.apk").get() }
}

tasks.matching { it.name == "assembleRelease" }.configureEach {
    finalizedBy("copyToDist")
}

tasks.matching { it.name == "optimizeReleaseResources" }.configureEach {
    doLast {
        val optimizeDir = layout.buildDirectory
            .dir("intermediates/optimized_processed_res/release/optimizeReleaseResources")
            .get()
            .asFile
        val expected = optimizeDir.resolve("resources-release-optimize.ap_")
        if (!expected.isFile) {
            val fallback = layout.buildDirectory
                .file("intermediates/shrunk_resources_binary_format/release/convertShrunkResourcesToBinaryRelease/shrunk-resources-binary-format-release.ap_")
                .get()
                .asFile
            if (fallback.isFile) {
                fallback.copyTo(expected, overwrite = true)
            }
        }
    }
}
