using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using crm.Models;
using Microsoft.AspNetCore.Identity.Data;

namespace crm.Controllers;

[Route("/login")]
public class LoginController : Controller
{
    private readonly ILogger<LoginController> _logger;

    public LoginController(ILogger<LoginController> logger)
    {
        _logger = logger;
    }

    [HttpGet("")]
    public IActionResult Login()
    {
        return View();
    }

    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] LoginRequest loginRequest)
    {
        var client = new HttpClient();
        var loginUrl = "http://localhost:8080/login/"; // URL of Spring Boot login endpoint

        var loginData = new
        {
            Username = loginRequest.Email,
            Password = loginRequest.Password
        };

        var response = await client.PostAsJsonAsync(loginUrl, loginData);
        
        if (response.IsSuccessStatusCode)
        {
            var token = await response.Content.ReadAsStringAsync();
            return Ok(new { Token = token }); // Return the JWT token
        }

        return Unauthorized();
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
