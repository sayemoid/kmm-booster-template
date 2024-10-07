package modules.common.ads

import androidx.compose.runtime.Composable
import modules.common.ads.Ads


actual class Ads {


	actual fun load(adUnitId: String) {
	}

	actual fun show() {
	}
}

@Composable
actual fun interstitialAd(adUnitId: String): Ads {
	val ad = Ads()
	ad.load(adUnitId)
	return ad
}