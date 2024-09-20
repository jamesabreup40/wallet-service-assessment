package br.com.recargapay.walletserviceassessment.domain

import java.math.BigDecimal

enum class TransactionType {
    DEPOSIT {
        override fun doTransaction(wallet: Wallet, amount: BigDecimal) {
            wallet.deposit(amount)
        }
    },
    WITHDRAW {
        override fun doTransaction(wallet: Wallet, amount: BigDecimal) {
            wallet.withdraw(amount)
        }
    };

    abstract fun doTransaction(wallet: Wallet, amount: BigDecimal)
}
