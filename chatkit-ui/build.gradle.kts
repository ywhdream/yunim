/*
 * Copyright (c) 2022 NetEase, Inc.  All rights reserved.
 * Use of this source code is governed by a MIT license that can be found in the LICENSE file.
 */

plugins {
    id("com.android.library")
    kotlin("android")
//    id("maven-publish")
    `maven-publish`
}

android {
    compileSdk = 33
    namespace = "com.netease.yunxin.kit.chatkit.ui"
    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "versionName", "\"10.3.2\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
    dataBinding {
        enable = true
    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets["main"].res.srcDirs("src/main/res", "src/main/res-fun", "src/main/res-normal")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api("com.github.tbruyelle:rxpermissions:v0.12")

    // imuikit 底层库
    api("com.netease.yunxin.kit.chat:chatkit:10.4.0")
    api("com.netease.yunxin.kit.common:common-ui:1.3.8")
    api("com.netease.yunxin.kit.core:corekit-plugin:1.1.3")

    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.airbnb.android:lottie:5.0.3")
    implementation("com.github.bumptech.glide:glide:4.13.1")
    implementation("com.alibaba:fastjson:1.2.48")
    implementation("io.reactivex.rxjava3:rxjava:3.0.4")
    implementation("androidx.databinding:databinding-runtime:4.2.0")


    /**  音视频2.0**/
    implementation("com.netease.yunxin:nertc-base:5.5.33")
    implementation("com.netease.yunxin.kit.call:call-ui:2.2.0") //呼叫组件 UI 包
    implementation("com.netease.nimlib:avsignalling:10.5.0") //信令组件

}

afterEvaluate {
//    publishing {
//        publications {
//            create<MavenPublication>("mavenJava") {
//                from(components["java"])
//                groupId = "com.github.ywhdream"
//                artifactId = "im"
//                version = "1.0.7"
//            }
//        }
//    }

    publishing {
        publications {
            // Creates a Maven publication called "release".
            register<MavenPublication>("release") {
                // 从 components.release 获取组件
                from(components["release"])
                groupId = "com.github.ywhdream"
                artifactId = "yunim"
                version = "1.1.3"
            }
        }
    }
}
