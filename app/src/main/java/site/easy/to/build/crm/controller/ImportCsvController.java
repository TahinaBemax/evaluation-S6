package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.google.model.calendar.EventDisplay;
import site.easy.to.build.crm.google.model.calendar.EventDisplayList;
import site.easy.to.build.crm.google.service.acess.GoogleAccessService;
import site.easy.to.build.crm.google.service.calendar.GoogleCalendarApiService;
import site.easy.to.build.crm.importcsv.ImportCsvDTO;
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
import java.util.List;

@Controller
@RequestMapping("manager/import-csv")
public class ImportCsvController {

    @GetMapping()
    public String importCsvPage(Model model, Authentication authentication) {
        model.addAttribute("importCsv", new ImportCsvDTO());
        return (!AuthorizationUtil.hasRole(authentication,"ROLE_MANAGER")) ? "error/access-denied" : "csv/import-csv";
    }
}
