package br.com.recargapay.walletserviceassessment.resource.dto

import br.com.recargapay.walletserviceassessment.domain.TransactionType
import java.math.BigDecimal

data class WalletTransactionDTO(
    val walletId: String,
    val amount: BigDecimal,
    val transactionType: TransactionType
)
