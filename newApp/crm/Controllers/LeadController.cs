using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using crm.Models;
using System.Text.Json;
using crm.Models.dto;

namespace crm.Controllers;

[Route("manager/leads")]
public class LeadController : Controller
{
    private readonly ILogger<LeadController> _logger;
    private readonly LeadService _leadService;

    public LeadController(ILogger<LeadController> logger, LeadService leadService)
    {
        _logger = logger;
        _leadService = leadService;
    }


    [HttpPost("{leadId}/delete")]
    public async Task<IActionResult> DeleteLeadAsync(int leadId)
    {
        bool reponse = await _leadService.DeleteLeadAsync(leadId);
        
        if (reponse)
        {
            return Redirect("/manager");
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
