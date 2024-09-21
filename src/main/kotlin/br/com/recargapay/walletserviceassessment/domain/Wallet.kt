package br.com.recargapay.walletserviceassessment.domain

import br.com.recargapay.walletserviceassessment.domain.exception.NegativeDepositAmountInWallet
import br.com.recargapay.walletserviceassessment.domain.exception.NoBalanceAvailableInWallet
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID.randomUUID

@Document(collection = "wallets")
class Wallet(
    @Id val id: String = randomUUID().toString(),
    var balance: BigDecimal = ZERO,
    @Version
    var version: Long? = null
) {

    fun cashIn(depositAmount: BigDecimal) =
        if (depositAmount <= ZERO) throw NegativeDepositAmountInWallet()
        else balance = balance.add(depositAmount)

    fun cashOut(withdrawAmount: BigDecimal) =
        if (balance < withdrawAmount) throw NoBalanceAvailableInWallet()
        else balance = balance.subtract(withdrawAmount)
}
