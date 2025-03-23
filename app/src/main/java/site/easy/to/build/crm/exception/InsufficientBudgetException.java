package site.easy.to.build.crm.exception;

import lombok.Getter;

@Getter
public class InsufficientBudgetException extends Exception{
    String budgetAmount;
    String expenseAmount;
    String message;

    public InsufficientBudgetException(double budgetAmount, double expenseAmount) {
        super();
        this.budgetAmount = String.valueOf(budgetAmount);
        this.expenseAmount = String.valueOf(expenseAmount);
        this.message = String.format("Budget insufficent! Budget amount: %s but espense amount: %s", budgetAmount, expenseAmount);
    }
}
