package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.importcsv.ImportCsvDTO;
import site.easy.to.build.crm.importcsv.exception.ImportCsvException;

import site.easy.to.build.crm.importcsv.service.DataPreparator;
import site.easy.to.build.crm.importcsv.service.DatabaseResetService;

import site.easy.to.build.crm.util.AuthorizationUtil;
import java.io.IOException;
import java.sql.SQLException;


@Controller
@RequestMapping("/manager")
public class ImportCsvController {
    private final DatabaseResetService databaseResetService;
    private final DataPreparator dataPreparator;

    @Autowired
    public ImportCsvController(DatabaseResetService databaseResetService, DataPreparator dataPreparator) {
        this.databaseResetService = databaseResetService;
        this.dataPreparator = dataPreparator;
    }

    @GetMapping("/import-csv")
    public String importCsvPage(Model model, Authentication authentication) throws SQLException {
        model.addAttribute("importCsv", new ImportCsvDTO());
        model.addAttribute("tablesname", databaseResetService.getDbTablesName());
        return (!AuthorizationUtil.hasRole(authentication,"ROLE_MANAGER")) ? "error/access-denied" : "csv/import-csv";
    }

    @GetMapping("/delete-data")
    public String reinitDatabase(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            if (databaseResetService.resetData()){
                redirectAttributes.addFlashAttribute("deleteDataScuccessMessage", "Database cleaned successfuly");
            } else {
                redirectAttributes.addFlashAttribute("deleteDataErrorMessage", "Failed to delete data");
            }
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("deleteDataErrorMessage", e.getMessage());
            throw new RuntimeException(e);
        }

        return (!AuthorizationUtil.hasRole(authentication,"ROLE_MANAGER")) ? "error/access-denied" : "redirect:/manager/import-csv";
    }


    @PostMapping("/upload")
    public String uploadData(@Validated
                                 @ModelAttribute("importCsv")
           ImportCsvDTO importCsvDTO,
           Authentication authentication,
           BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException
    {
        if (!AuthorizationUtil.hasRole(authentication,"ROLE_MANAGER")){
            return "error/access-denied";
        }

        if (bindingResult.hasErrors()){
            return "csv/import-csv";
        }

        try {
            dataPreparator.initParse(importCsvDTO.getCustomerFile(),importCsvDTO.getBudgetFile(),importCsvDTO.getTicketFile(), importCsvDTO.getSeparator());
            dataPreparator.uploadData();
            redirectAttributes.addFlashAttribute("importSuccessMessage","Data uploaded successfuly.");
        } catch (ImportCsvException e) {
            redirectAttributes.addFlashAttribute("importErrorMessage","Can't upload csv file." + e.getMessage());
        }
        return "redirect:/manager/import-csv";
    }
}
