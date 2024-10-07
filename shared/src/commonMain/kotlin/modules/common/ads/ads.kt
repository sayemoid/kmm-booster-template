package modules.common.ads

import androidx.compose.runtime.Composable

expect class Ads {
	fun load(adUnitId: String)

	fun show()
}

@Composable
expect fun interstitialAd(adUnitId: String): Ads
