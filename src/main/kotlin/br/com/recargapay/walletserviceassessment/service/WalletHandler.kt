package br.com.recargapay.walletserviceassessment.service

import br.com.recargapay.walletserviceassessment.domain.Wallet
import br.com.recargapay.walletserviceassessment.domain.exception.NoBalanceAvailableInWallet
import br.com.recargapay.walletserviceassessment.repository.WalletRepository
import br.com.recargapay.walletserviceassessment.resource.dto.GlobalTransactionDTO
import br.com.recargapay.walletserviceassessment.resource.dto.WalletTransactionDTO
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpStatus.CREATED
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.notFound
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
        request.let {
            return repository.save(Wallet()).let {
                log.info("Wallet id {} created", it.id)
                responseWithStatus(CREATED).bodyValueAndAwait(it.id)
            }
        }
    }

    suspend fun transfer(request: ServerRequest): ServerResponse {
        return repository.findById(requestedWalletId(request))?.let { sourceWalletForTransfer ->
            requestedGlobalTransaction(request).let {
                try {
                    repository.findById(it.targetWalletId)?.let { targetWalletForTransfer ->
                        it.transactionType.`do`(sourceWalletForTransfer, targetWalletForTransfer, it.amount)
                        repository.save(sourceWalletForTransfer)
                        repository.save(targetWalletForTransfer)
                        log.info(
                            "TRANSFER from Wallet id {} for Wallet id {}", requestedWalletId(request),
                            requestedWalletId(request)
                        )
                        ok().bodyValueAndAwait("")
                    } ?: notFound().buildAndAwait()
                } catch (exception: Exception) {
                    when (exception) {
                        is NoBalanceAvailableInWallet, is NegativeArraySizeException -> badRequest().buildAndAwait()
                        else -> throw exception
                    }
                }
            }
        } ?: notFound().buildAndAwait()
    }

    suspend fun funds(request: ServerRequest): ServerResponse {
        return repository.findById(requestedWalletId(request))?.let { walletForTransaction ->
            requestedWalletTransaction(request).let {
                try {
                    it.transactionType.`do`(walletForTransaction, it.amount)
                    repository.save(walletForTransaction)
                    log.info("{} in Wallet id {}", it.transactionType, requestedWalletId(request))
                    ok().bodyValueAndAwait("")
                } catch (exception: Exception) {
                    when (exception) {
                        is NoBalanceAvailableInWallet, is NegativeArraySizeException -> badRequest().buildAndAwait()
                        else -> throw exception
                    }
                }
            }
        } ?: notFound().buildAndAwait()
    }

    suspend fun balance(request: ServerRequest): ServerResponse {
        return repository.findById(requestedWalletId(request))?.let {
            it.let { ok().bodyValueAndAwait(it.balance) }
        } ?: notFound().buildAndAwait()
    }

    private suspend fun requestedGlobalTransaction(request: ServerRequest) =
        request.awaitBody<GlobalTransactionDTO>()

    private suspend fun requestedWalletTransaction(request: ServerRequest) =
        request.awaitBody<WalletTransactionDTO>()

    private fun requestedWalletId(request: ServerRequest) = request.pathVariable("id")
}