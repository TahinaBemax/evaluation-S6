package site.easy.to.build.crm.exception;

import lombok.Getter;

@Getter
public class BudgetNotFoundException extends Exception{
    String message;
    public BudgetNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public BudgetNotFoundException(Integer id){
        super();
        this.message = String.format("Budget id %s not found!", String.valueOf(id));
    }
}
