package modules.common.ads

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import configs.Credentials

actual class Ads(
	private val context: ComponentActivity
) {
	private var mInterstitialAd: InterstitialAd? = null
	private final val TAG = "Ads"

	actual fun load(adUnitId: String) {
		val adRequest = AdRequest.Builder().build()

		InterstitialAd.load(
			context,
			adUnitId,
			adRequest,
			object : InterstitialAdLoadCallback() {
				override fun onAdFailedToLoad(adError: LoadAdError) {
					mInterstitialAd = null
				}

				override fun onAdLoaded(interstitialAd: InterstitialAd) {
					mInterstitialAd = interstitialAd
				}
			})

		mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
			override fun onAdClicked() {
				// Called when a click is recorded for an ad.
			}

			override fun onAdDismissedFullScreenContent() {
				// Called when ad is dismissed.
				mInterstitialAd = null
			}

			override fun onAdFailedToShowFullScreenContent(p0: AdError) {
				mInterstitialAd = null
			}

			override fun onAdImpression() {
				// Called when an impression is recorded for an ad.
			}

			override fun onAdShowedFullScreenContent() {
				// Called when ad is shown.
			}
		}
	}

	actual fun show() {
		this.mInterstitialAd?.show(this.context)
	}
}

@Composable
actual fun interstitialAd(adUnitId: String): Ads {
	val context = LocalContext.current as ComponentActivity
	val ad = Ads(context)
	ad.load(adUnitId)
	return ad
}