using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using crm.Models;
using System.Text.Json;
using crm.Models.dto;

namespace crm.Controllers;

[Route("manager")]
public class TicketController : Controller
{
    private readonly ILogger<TicketController> _logger;
    private readonly DashboardService _dashboardService;
    private readonly CustomerService _customerService;
    private readonly TicketService _ticketService;

    public TicketController(ILogger<TicketController> logger, DashboardService dashboardService, CustomerService customerService, TicketService leadService)
    {
        _logger = logger;
        _dashboardService = dashboardService;
        _customerService = customerService;
        _ticketService = leadService;
    }


    [HttpGet("tickets/monthly-statistic")]
    public async Task<IActionResult> GetMonthlyStatistiqueAsync(int? year)
    {
        try
        {
            List<StatisticLead> statistics = await _ticketService.GetMonthlyStatistiqueTicketAsync(year);
            decimal sommeAmount = statistics.Sum( s => s.TotalAmount);
            decimal sommeLead = statistics.Sum( s => s.Total);
            ViewData["statistics"] = statistics;
            ViewData["selectedYear"] = year;
            ViewData["sommeAmount"] = sommeAmount;
            ViewData["sommeLead"] = sommeLead;
            ViewData["sommeLead"] = sommeLead;
            ViewData["page"] = "ticket";

            return View("~/Views/dashboard/TicketLead.cshtml", statistics);
        }
        catch (Exception ex)
        {
            ViewBag.ErrorMessage = ex.Message;
            return View("Error");
        }
    }

    [HttpGet("tickets/monthly-statistic-detail")]
    public async Task<IActionResult> GetDetailMonthlyStatistiquetTicketAsync(int? year)
    {
        try
        {
            List<DetailStatisticLead> statistics = await _ticketService.GetDetailMonthlyStatistiqueTicketAsync(year);
            ViewData["statistics"] = statistics;
            ViewData["selectedYear"] = year;
            ViewData["page"] = "ticket";

            return View("~/Views/dashboard/List.cshtml");
        }
        catch (Exception ex)
        {
            ViewBag.ErrorMessage = ex.Message;
            return View("Error");
        }
    }

    [HttpGet("customers/{id}/budgets")]
    public IActionResult CustomerBudgets(int id, DateTime? startDate, DateTime? end)
    {
        return View("~/Views/budget/list.cshtml");
    }
    
    [HttpGet("customers/{id}/expenses")]
    public async Task<IActionResult> CustomerExpenses(int id, DateTime? startDate, DateTime? end)
    {
        List<Expense>? expenses = await _dashboardService.GetCustomersExepenseBetweenDateAsync(id, startDate, end);
        ViewData["expenses"] = expenses;
        return View("~/Views/expenses/list.cshtml");
    }

    [HttpGet("customers/{id}/tickets")]
    public async Task<IActionResult> GetCustomerTickets(int id, DateTime? startDate, DateTime? end)
    {
        List<Dictionary<string, object>>? tickets = await _dashboardService.GetCustomersTicketBetweenDateAsync(id, startDate, end);
        ViewData["tickets"] = tickets;
        foreach (var t in tickets?? [])
        {
            t["customer"] = JsonSerializer.Deserialize<Customer>(t["customer"]?.ToString());
            t["employee"] = JsonSerializer.Deserialize<Dictionary<string, object>>(t["employee"]?.ToString())["username"];
        }
        return View("~/Views/ticket/list.cshtml");
    }

    
    
    [HttpGet("tickets")]
    public async Task<IActionResult> TicketStatistic(int? year,int? id, DateTime? startDate, DateTime? endDate)
    {
        // Initialiser un tableau avec 0 pour chaque mois
        int[] ticketsPerMonthArray = new int[12];

        // Vérifier si totalParMois contient des données
        List<TicketStat>? totalParMois = await _dashboardService.GetTicketStatisticAsync(year);

        if (totalParMois != null)
        {
            foreach (var t in totalParMois)
            {
                    if (t.Month >= 1 && t.Month <= 12)  // Vérifier que le mois est valide
                    {
                        ticketsPerMonthArray[t.Month - 1] = t.Total;  // Stocker la valeur
                    }
            }
        }
        
        ViewData["totalPerMonth"] = ticketsPerMonthArray;
        ViewData["year"] = year;
        ViewData["startDate"] = startDate;
        ViewData["endDate"] = endDate;
        ViewData["customerId"] = id;
        ViewData["tickets"] = await _dashboardService.GetTicketPerCustomerAsync(id, startDate, endDate);
        ViewData["customers"] = await _customerService.GetCustomersAsync();
        
        return View("~/Views/dashboard/ticket.cshtml");
    }
    
    public IActionResult Privacy()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
