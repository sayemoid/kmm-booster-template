package configs

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = CIO