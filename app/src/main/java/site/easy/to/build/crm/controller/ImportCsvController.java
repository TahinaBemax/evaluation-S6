package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.google.model.calendar.EventDisplay;
import site.easy.to.build.crm.google.model.calendar.EventDisplayList;
import site.easy.to.build.crm.google.service.acess.GoogleAccessService;
import site.easy.to.build.crm.google.service.calendar.GoogleCalendarApiService;
import site.easy.to.build.crm.importcsv.ImportCsvDTO;
import site.easy.to.build.crm.importcsv.service.DatabaseResetService;
import site.easy.to.build.crm.service.contract.ContractService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.weather.WeatherService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ImportCsvController {
    DatabaseResetService databaseResetService;

    @Autowired
    public ImportCsvController(DatabaseResetService databaseResetService) {
        this.databaseResetService = databaseResetService;
    }

    @GetMapping("/import-csv")
    public String importCsvPage(Model model, Authentication authentication) {
        model.addAttribute("importCsv", new ImportCsvDTO());
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

    @PostMapping("/import-csv")
    public String uploadData(@Validated
                                 @ModelAttribute("importCsv")
                                 @RequestParam("") ImportCsvDTO importCsvDTO,Authentication authentication, RedirectAttributes redirectAttributes)
    {
        redirectAttributes.addFlashAttribute("successMessage","File uploaded successfuly.");
        //redirectAttributes.addFlashAttribute("errorMessage","Can't upload csv file.");
        return (!AuthorizationUtil.hasRole(authentication,"ROLE_MANAGER")) ? "error/access-denied" : "redirect:/csv/import-csv";
    }
}
