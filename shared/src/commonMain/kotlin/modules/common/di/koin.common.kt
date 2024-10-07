package modules.common.di

import configs.Credentials
import modules.common.AuthCredentials
import modules.common.features.auth.AuthRepository
import modules.common.features.auth.AuthRepositoryImpl
import modules.common.features.auth.AuthService
import modules.common.features.auth.AuthServiceImpl
import modules.common.features.auth.AuthVM
import modules.common.features.preferences.PrefVM
import modules.common.libModule
import modules.common.routes.Routes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

val commonModule = module {

	/*
	Auth Components
	 */
	single<AuthRepository> { AuthRepositoryImpl(get()) }
	single<AuthService> { AuthServiceImpl(get()) }
	// AuthVM needs to be singleton because auth is used in every screens and ViewModel
	// needs to survive as long as app is running on the foreground, so the auth state isn't lost.
	single { AuthVM(get(), get(), get()) }

	/*
	Preference/Settings Components
	 */
	factory { PrefVM(get()) }

} + libModule(
	AuthCredentials(
		tokenUrl = Routes.GET_TOKEN,
		clientId = Credentials.Auth.clientId.get(),
		clientSecret = Credentials.Auth.clientSecret.get()
	)
)

inline fun <reified T> getKoinInstance(): T {
	return object : KoinComponent {
		val value: T by inject()
	}.value
}


