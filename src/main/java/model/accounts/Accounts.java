package model.accounts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.boot.buildpack.platform.io.Owner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Accounts implements Serializable {
    private final static Money PENALTY_FEE = new Money(BigDecimal.valueOf(50.));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonManagedReference
    private Long id;

    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    private Money balance;
    @ManyToOne(optional = false)
    private Owner primaryOwner;
    @ManyToOne(optional = true)
    private Owner secondaryOwner;
    @CreationTimestamp
    private LocalDateTime creationDateTime;
    private LocalDateTime lastAccessDateTime;

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonBackReference
    private List<Transaction> depositTxs;
    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JsonBackReference
    private List<Transaction> withdrawalTxs;

    @Enumerated(EnumType.STRING)
    private Type type;

    public Accounts(){
        setBalance(new Money(BigDecimal.ZERO));

        setCreationDateTime(LocalDateTime.now());
        setLastAccessDateTime(getCreationDateTime());

        setDepositTxs(new ArrayList<>());
        setWithdrawalTxs(new ArrayList<>());
    }

    public Accounts(Owner primaryOwner){
        this();
        setPrimaryOwner(primaryOwner);
    }

    public Accounts(Owner primaryOwner, Owner secondaryOwner){
        this(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    public Accounts(Owner primaryOwner, Money balance){
        this(primaryOwner);
        setBalance(balance);
    }
    public Accounts(Owner primaryOwner, Owner secondaryOwner, Money balance){
        this(primaryOwner, secondaryOwner);
        setBalance(balance);
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Money getBalance(){
        return balance;
    }

    public void setBalance(Money balance){
        this.balance = balance;
    }

    public Owner getPrimaryOwner(){
        return primaryOwner;
    }

    public void setPrimaryOwner(Owner primaryOwner){
        if(primaryOwner == null)
            return;
        this.primaryOwner = primaryOwner;
        primaryOwner.addPrimaryAccount(this);
    }

    public Owner getSecondaryOwner(){
        return secondaryOwner;
    }

    public void setSecondaryOwner(Owner secondaryOwner) {
        if (secondaryOwner == null)
            return;
        this.secondaryOwner = secondaryOwner;
        secondaryOwner.addSecondaryAccount(this);
    }
}