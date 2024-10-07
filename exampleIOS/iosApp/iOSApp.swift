import SwiftUI
import shared

@main
struct iOSApp: App {

     init() {
         Koin_exampleKt.doInitKoin()
     }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
