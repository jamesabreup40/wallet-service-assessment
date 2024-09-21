package br.com.recargapay.walletserviceassessment.domain

import java.math.BigDecimal

enum class GlobalTransactionType {
    TRANSFER {
        override fun `do`(sourceWallet: Wallet, targetWallet: Wallet, amount: BigDecimal) {
            sourceWallet.cashOut(amount)
            targetWallet.cashIn(amount)
        }
    };

    abstract fun `do`(sourceWallet: Wallet, targetWallet: Wallet, amount: BigDecimal)

}
