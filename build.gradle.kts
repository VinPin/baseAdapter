buildscript {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    }
}

allprojects {
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}