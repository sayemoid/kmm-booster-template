import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.jetbrainsCompose)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.serialization)
}

kotlin {
	androidTarget()

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach { iosTarget ->
		iosTarget.binaries.framework {
			baseName = "shared"
			isStatic = true
		}
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(compose.runtime)
				implementation(compose.foundation)
				api(compose.material3)
				api(compose.materialIconsExtended)
				@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
				implementation(compose.components.resources)

				implementation(project(":library"))
//				implementation(libs.cognito.clientlib)

				api(libs.voyager.navigator)
				api(libs.voyager.koin)

				// https://github.com/qdsfdhvh/compose-imageloader
				api(libs.image.loader)

				api(libs.aaychart)
			}
		}
		val androidMain by getting {
			dependencies {
				api(libs.compose.ui)
				api(libs.compose.ui.tooling)
				api(libs.compose.ui.tooling.preview)
				api(libs.androidx.activity.compose)

				api(libs.androidx.core.ktx)
				api(libs.coil.compose)
				api(libs.coil.svg)

				// Import the Firebase BoM
				api(project.dependencies.platform(libs.firebase.bom))
				// When using the BoM, don't specify versions in Firebase dependencies
				api(libs.firebase.analytics.ktx)
				api(libs.firebase.crashlytics.ktx)
				api(libs.firebase.messaging.ktx)
				api(libs.play.service.ads)
			}
		}
		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting
		val iosMain by creating {
			dependencies {
				implementation(libs.ktor.client.darwin)
			}
		}
	}

	@OptIn(ExperimentalKotlinGradlePluginApi::class)
	compilerOptions {
		freeCompilerArgs.add("-Xexpect-actual-classes")
	}
}

android {
	namespace = "com.example.shared"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
	sourceSets["main"].res.srcDirs("src/androidMain/res")
	sourceSets["main"].resources.srcDirs("src/commonMain/resources")

	defaultConfig {
		minSdk = libs.versions.android.minSdk.get().toInt()
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
			excludes += "META-INF/versions/9/previous-compilation-data.bin"
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		 jvmToolchain(libs.versions.jdk.get().toInt())
	}
}
