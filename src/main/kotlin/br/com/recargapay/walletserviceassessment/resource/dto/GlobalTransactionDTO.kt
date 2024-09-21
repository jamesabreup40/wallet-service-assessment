package br.com.recargapay.walletserviceassessment.resource.dto

import br.com.recargapay.walletserviceassessment.domain.GlobalTransactionType
import java.math.BigDecimal

data class GlobalTransactionDTO(
    val targetWalletId: String,
    val amount: BigDecimal,
    val transactionType: GlobalTransactionType
)
