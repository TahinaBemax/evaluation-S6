package site.easy.to.build.crm.importcsv.exception;


import lombok.Getter;
import java.util.List;

@Getter
public class ImportCsvException extends Exception{
    List<String> messages;
    public ImportCsvException(List<String> message, int ligne, String fileName) {
        super(String.format("There is an error line at %s, fileName: %s \n Error: %s", String.valueOf(ligne), fileName,message.toString()));
    }

    public ImportCsvException() {
        this.messages = messages;
    }
}
