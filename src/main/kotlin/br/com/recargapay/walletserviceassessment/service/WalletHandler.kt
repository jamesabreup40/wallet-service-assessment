package br.com.recargapay.walletserviceassessment.service

import br.com.recargapay.walletserviceassessment.domain.Wallet
import br.com.recargapay.walletserviceassessment.domain.exception.NoBalanceAvailableInWallet
import br.com.recargapay.walletserviceassessment.repository.WalletRepository
import br.com.recargapay.walletserviceassessment.resource.dto.WalletTransactionDTO
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus.CREATED
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.noContent
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.ServerResponse.status as responseWithStatus

@Service
class WalletHandler(val repository: WalletRepository) {

    companion object {
        @JvmStatic
        private val log = getLogger(Companion::class.java)
    }

    suspend fun create(request: ServerRequest): ServerResponse {
        log.debug("Creating a new wallet")
        request.let {
            return repository.save(Wallet()).let {
                log.info("Wallet id {} created", it.id)
                responseWithStatus(CREATED).bodyValue(it.id).awaitSingle()
            }
        }
    }

    suspend fun transaction(request: ServerRequest): ServerResponse {
        log.debug("Transaction amount in wallet")
        return repository.findById(request.pathVariable("id")).let { foundWallet ->
            request.awaitBody<WalletTransactionDTO>().let {
                try {
                    log.info("${it.transactionType} in Wallet id {}", request.pathVariable("id"))
                    it.transactionType.doTransaction(foundWallet!!, it.amount)
                    ok().bodyValueAndAwait("")
                } catch (exception: Exception) {
                    when (exception) {
                        is NoBalanceAvailableInWallet, is NegativeArraySizeException -> badRequest().buildAndAwait()
                        else -> throw exception
                    }
                }
            }
        }
    }

    suspend fun balance(request: ServerRequest): ServerResponse {
        log.debug("Returning balance from wallet")
        return repository.findById(request.pathVariable("id")).let {
            it?.let { ok().bodyValueAndAwait(it.balance) }
        } ?: noContent().buildAndAwait()
    }
}