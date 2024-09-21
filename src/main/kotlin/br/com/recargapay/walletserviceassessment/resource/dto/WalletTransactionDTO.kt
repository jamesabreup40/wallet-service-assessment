package br.com.recargapay.walletserviceassessment.resource.dto

import br.com.recargapay.walletserviceassessment.domain.WalletTransactionType
import java.math.BigDecimal

data class WalletTransactionDTO(
    val amount: BigDecimal,
    val transactionType: WalletTransactionType
)
