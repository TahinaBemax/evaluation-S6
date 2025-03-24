using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using crm.Models;

public class DashboardService
{
    private readonly HttpClient _httpClient;

    public DashboardService(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<Dashboard> GetBudgetStatisticAsync()
    {
        var response = await _httpClient.GetAsync("http://localhost:8080/api/crm/budgets");
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<Dashboard>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
    }
}
