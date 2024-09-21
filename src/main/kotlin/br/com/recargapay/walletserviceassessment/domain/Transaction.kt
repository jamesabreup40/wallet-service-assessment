package br.com.recargapay.walletserviceassessment.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.UUID.randomUUID

@Document(collection = "transactions")
data class Transaction(
    val id: String = randomUUID().toString(),
    val walletId: String,
    val amount: BigDecimal,
    val walletTransactionType: WalletTransactionType
)
