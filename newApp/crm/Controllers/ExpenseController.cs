using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using crm.Models;
using System.Text.Json;
using crm.Models.dto;

namespace crm.Controllers;

[Route("manager/expenses")]
public class ExpenseController : Controller
{
    private readonly ILogger<ExpenseController> _logger;
    private readonly DashboardService _dashboardService;
    private readonly TicketService _ticketService;
    private readonly LeadService _leadService;

    public ExpenseController(ILogger<ExpenseController> logger, DashboardService dashboardService, TicketService TicketService, LeadService leadService)
    {
        _logger = logger;
        _dashboardService = dashboardService;
        _ticketService = TicketService;
        _leadService = leadService;
    }



    [HttpGet("lead/{expenseId}/edit")]
    public async Task<IActionResult> FormLead(int expenseId)
    {
        DetailStatisticLead? detail = await _leadService.GetDetailMonthlyStatLeadByExpenseIdAsync(expenseId);

            ViewData["detail"] = detail;
        if(detail == null){
            return Error();
        } else {
            return View("~/Views/expense/Form.cshtml", detail);
        }
    }

    [HttpGet("ticket/{expenseId}/edit")]
    public async Task<IActionResult> FormTicket(int expenseId)
    {
        DetailStatisticLead? detail = await _ticketService.GetDetailMonthlyStatTicketByExpenseIdAsync(expenseId);

        ViewData["detail"] = detail;
        if(detail == null){
            return Error();
        } else {
            return View("~/Views/expense/Form.cshtml");
        }
    }

    [HttpPost("{expenseId}/save-edit")]
    public async Task<IActionResult> update(int expenseId, decimal Amount)
    {
        DetailStatisticLead? detail = await _leadService.UpdateExpenseAmountAsync(expenseId, Amount);

        if(detail == null){
            return Error();
        } else {
            return Redirect("/manager");
        }
    }


    [HttpPost("{expenseId}/delete")]
    public async Task<IActionResult> DeleteCustomerTicket(int expenseId, int isTicket)
    {
        bool reponse = await _dashboardService.DeleteCustomerTicketOrLead(expenseId, isTicket);
        
        if (reponse)
        {
            if(isTicket == 1){
                return Redirect("/manager/tickets/monthly-statistic-detail");
            } 
            return Redirect("/manager/leads/monthly-statistic-detail");
        }
        TempData["ErrorMessage"] = "UNe erreur est survenue";

        var referer = Request.Headers.Referer.ToString();
        return Redirect(referer);
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
