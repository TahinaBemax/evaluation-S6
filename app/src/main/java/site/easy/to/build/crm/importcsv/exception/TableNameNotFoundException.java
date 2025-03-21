package site.easy.to.build.crm.importcsv.exception;

import lombok.Getter;

@Getter
public class TableNameNotFoundException extends Exception{
    String message;
    String tableName;

    public TableNameNotFoundException(String message, String tableName) {
        super(message);
        this.message = message;
        this.tableName = tableName;
    }

    public TableNameNotFoundException(String tableName) {
        this.message = "Nom de table [" +tableName + "] introuvable.";
        this.tableName = tableName;
    }
}
