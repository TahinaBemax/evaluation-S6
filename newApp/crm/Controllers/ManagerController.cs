using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using crm.Models;

namespace crm.Controllers;

[Route("manager")]
public class ManagerController : Controller
{
    private readonly ILogger<ManagerController> _logger;

    public ManagerController(ILogger<ManagerController> logger)
    {
        _logger = logger;
    }

    [HttpGet("")]
    public IActionResult Index()
    {
        return View("~/Views/dashboard/index.cshtml");
    }

    [HttpGet("customer/{id}/budgets")]
    public IActionResult CustomerBudgets(int id, DateTime? startDate, DateTime? end)
    {
        return View("~/Views/budget/list.cshtml");
    }
    
    [HttpGet("customer/{id}/expenses")]
    public IActionResult CustomerExpenses(int id, DateTime? startDate, DateTime? end)
    {
        return View("~/Views/budget/list.cshtml");
    }

    [HttpGet("tickets")]
    public IActionResult Tickets(int? id, DateTime? startDate, DateTime? end)
    {
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
