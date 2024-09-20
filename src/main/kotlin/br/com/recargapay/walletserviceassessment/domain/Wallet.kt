package br.com.recargapay.walletserviceassessment.domain

import br.com.recargapay.walletserviceassessment.domain.exception.NegativeDepositAmountInWallet
import br.com.recargapay.walletserviceassessment.domain.exception.NoBalanceAvailableInWallet
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID.randomUUID

@Document(collection = "wallets")
class Wallet(
    val id: String = randomUUID().toString(),
    var balance: BigDecimal = ZERO
) {

    fun deposit(depositAmount: BigDecimal) =
        if (depositAmount < ZERO) throw NegativeDepositAmountInWallet()
            else balance.plus(depositAmount)

    fun withdraw(withdrawAmount: BigDecimal) =
        if (balance < withdrawAmount) throw NoBalanceAvailableInWallet()
            else balance.minus(withdrawAmount)
}
