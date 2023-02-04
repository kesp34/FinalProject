package model.accounts;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import model.accounts.Accounts;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "id")
public class CheckingAccount extends Accounts implements WithMonthlyFee, WithSecretKey, WithStatus{

}
