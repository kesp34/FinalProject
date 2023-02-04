package model.accounts;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
@Data
@Embeddable
public class Money {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency CHF = Currency.getInstance("CHF");
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    private final Currency currency;
    private BigDecimal amount;
    public Money(BigDecimal amount, Currency eur, RoundingMode defaultRounding){
        currency = EUR;
    }

    public Money(BigDecimal amount, Currency currency, RoundingMode rounding){
        this.currency = currency;
        setAmount(amount.setScale(currency.getDefaultFractionDigits(), rounding));
    }

    public Money(BigDecimal amount, Currency currency){
        this.amount = amount;
        this.currency = currency;
    }

    private BigDecimal amount;
    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private Currency currency = Currency.getInstance("EUR");
    public Money(Currency currency, BigDecimal amount){
        this.currency = currency;
        this.amount = amount.setScale(this.currency.getDefaultFractionDigits(), roundingMode);
    }
    public Money(BigDecimal amount){
        this(amount, EUR, DEFAULT_ROUNDING);
    }

    public BigDecimal increaseAmount(Money money){
        setAmount(this.amount.add(money.amount));
        return this.amount;
    }

    public BigDecimal increaseAmount(BigDecimal addAmount){
        setAmount(this.amount.add(addAmount));
        return this.amount;
    }

    public BigDecimal decreaseAmount(BigDecimal addAmount){
        setAmount(this.amount.subtract(addAmount));
        return this.amount;
    }

    public Currency getCurrency(){
        return this.currency;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }

    private void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public String toString(){
        return getCurrency().getSymbol() + " " + getAmount();
    }
}
