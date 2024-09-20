package br.com.recargapay.walletserviceassessment.repository

import br.com.recargapay.walletserviceassessment.domain.Wallet
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface WalletRepository : CoroutineCrudRepository<Wallet, String>
