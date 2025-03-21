package site.easy.to.build.crm.importcsv.exception;


import lombok.Getter;
import java.util.List;

@Getter
public class ImportCsvException extends Exception{
    List<String> messages;
    public ImportCsvException(List<String> message, int ligne) {
        super("Il y a une erreur dans la ligne " + ligne + ".\n Erreur: " + message.toString());
    }

}
