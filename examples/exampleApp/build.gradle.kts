plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsCompose)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.crashlytics)
}
/*
Conditionally apply google services plugin based on existence of google-services.json file
 */
val googleServicesFile = File(projectDir, "google-services.json")
if (googleServicesFile.exists()) {
	println("google-services.json found, Google Services will be enabled.")
	apply(plugin = libs.plugins.googleServices.get().pluginId)
} else {
	println("google-services.json not found, Google Services will be disabled.")
}
/*
End loading google services plugin
 */

kotlin {
	androidTarget()
	sourceSets {
		val androidMain by getting {
			dependencies {
				implementation(project(":shared"))
			}
		}
	}
}

android {
	compileSdk = (findProperty("android.compileSdk") as String).toInt()
	namespace = "org.cognitox"

	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

	defaultConfig {
		applicationId = "$namespace"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
			excludes += "META-INF/versions/9/previous-compilation-data.bin"
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		jvmToolchain(libs.versions.jdk.get().toInt())
	}

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
	}

	buildFeatures {
		compose = true
	}

//    buildTypes {
//        release {
//            isMinifyEnabled = true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
}
