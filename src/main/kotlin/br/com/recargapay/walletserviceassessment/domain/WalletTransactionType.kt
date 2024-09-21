package br.com.recargapay.walletserviceassessment.domain

import java.math.BigDecimal

enum class WalletTransactionType {
    DEPOSIT {
        override fun `do`(wallet: Wallet, amount: BigDecimal) {
            wallet.cashIn(amount)
        }
    },
    WITHDRAW {
        override fun `do`(wallet: Wallet, amount: BigDecimal) {
            wallet.cashOut(amount)
        }
    };

    abstract fun `do`(wallet: Wallet, amount: BigDecimal)
}
