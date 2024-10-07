package modules.exampleModule.di

import modules.common.di.commonModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

val exampleModule = module {

	/*
	Register koin components here
	 */

}

val koinExampleModules = commonModule + exampleModule

fun initKoin() = startKoin { modules(koinExampleModules) }