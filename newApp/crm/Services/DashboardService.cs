using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using crm.Models;
using crm.Models.dto;
using Microsoft.AspNetCore.Mvc.Routing;

public class DashboardService
{
    private readonly HttpClient _httpClient;

    public DashboardService(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<Dashboard?> GetBudgetStatisticAsync(int? custumerId, DateTime? start,  DateTime? end)
    {
        string url = "http://localhost:8080/api/crm/budgets";
        url = PrepareUrl(url ,custumerId,start, end);

        var response = await _httpClient.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        if(!string.IsNullOrEmpty(json)){
            return JsonSerializer.Deserialize<Dashboard>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        return null;
    }

    public async Task<List<TicketStat>?> GetTicketStatisticAsync(int? year)
    {
        string url = "http://localhost:8080/api/crm/customers/tickets/statistic-global";
        if(year != null){
            url += $"?year={year}";
        }

        var response = await _httpClient.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        if(!string.IsNullOrEmpty(json)){
            return JsonSerializer.Deserialize<List<TicketStat>?>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        return null;
    }

    public async Task<List<Dictionary<string, object>>?> GetTicketPerCustomerAsync(int? custumerId, DateTime? start,  DateTime? end)
    {
        string url = "http://localhost:8080/api/crm/customers/tickets/statistic";
        url = PrepareUrl(url ,custumerId, start, end);

        var response = await _httpClient.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        if(!string.IsNullOrEmpty(json)){
            return JsonSerializer.Deserialize<List<Dictionary<string, object>>>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        return null;
    }

    public async Task<bool> DeleteCustomerTicket(int Id)
    {
        string url = $"http://localhost:8080/api/crm/customers/tickets/{Id}";

        var response = await _httpClient.DeleteAsync(url);
        if(response.IsSuccessStatusCode){
            return true;
        }
        return false;
    }

    public async Task<List<Dictionary<string, object>>?> GetCustomersTicketBetweenDateAsync(int custumerId, DateTime? start,  DateTime? end)
    {
        string url = $"http://localhost:8080/api/crm/customers/{custumerId}/tickets";

        if(start != null){
            url += $"?startDate={start?.Date.ToString("yyyy-MM-ddTHH:mm")}";
        }

        if(end != null){
            if(start == null){
                url += $"?endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            } else {
                url += $"&endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            }
        }

        var response = await _httpClient.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        if(!string.IsNullOrEmpty(json)){
            return JsonSerializer.Deserialize<List<Dictionary<string, object>>>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        return null;
    }

    public async Task<List<Expense>?> GetCustomersExepenseBetweenDateAsync(int custumerId, DateTime? start,  DateTime? end)
    {
        string url = $"http://localhost:8080/api/crm/customers/{custumerId}/expenses";

        if(start != null){
            url += $"?startDate={start?.Date.ToString("yyyy-MM-ddTHH:mm")}";
        }

        if(end != null){
            if(start == null){
                url += $"?endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            } else {
                url += $"&endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            }
        }

        var response = await _httpClient.GetAsync(url);
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        if(!string.IsNullOrEmpty(json)){
            return JsonSerializer.Deserialize<List<Expense>>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
        }

        return null;
    }



    private static string PrepareUrl(string url, int? custumerId, DateTime? start,  DateTime? end){
        if(custumerId != null){
            url += $"?customerId={custumerId}";
        }

        if(start != null && custumerId == null){
            url += $"?startDate={start?.Date.ToString("yyyy-MM-ddTHH:mm")}";
        } else if(start != null && custumerId != null){
            url += $"&startDate={start?.Date.ToString("yyyy-MM-ddTHH:mm")}";
        }

        if(end != null){
            if(start == null && custumerId == null){
                url += $"?endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            } else if(start != null || custumerId != null){
                url += $"&endDate={end?.Date.ToString("yyyy-MM-ddTHH:mm")}";
            }
        }
        return url;
    }    
}
