package br.com.recargapay.walletserviceassessment.resource

import br.com.recargapay.walletserviceassessment.service.WalletHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.coRouter

@RestController
class WalletResource(val handler: WalletHandler) {

    @Bean
    fun resources() = coRouter {
        "/wallet".nest {
            accept(APPLICATION_JSON).nest {
                POST("/create", handler::create)
                POST("/{id}/transaction", handler::transaction)
                GET("/{id}/balance", handler::balance)
            }
        }
    }
}