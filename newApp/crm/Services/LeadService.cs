using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using crm.Models;
using crm.Models.dto;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Routing;

public class LeadService
{
    private readonly HttpClient _httpClient;
    private readonly ILogger<CustomerService> _logger;

    public LeadService(HttpClient httpClient, ILogger<CustomerService> logger)
    {
        _httpClient = httpClient;
        _logger = logger;
    }


    public async Task<List<StatisticLead>> GetMonthlyStatistiqueLeadAsync(int? year)
    {
        try
        {
            string url = "http://localhost:8080/api/crm/customers/leads/monthly-statistic";

            if(year.HasValue){
                url += $"?year={year}";
            }

            HttpResponseMessage response = await _httpClient.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError($"API error: {response.StatusCode}");
                throw new HttpRequestException($"API request failed with status code {response.StatusCode}");
            }

            var apiResponse = await response.Content.ReadFromJsonAsync<ApiResponse<List<StatisticLead>>>();
            return apiResponse?.Data ?? [];
        }
        catch (HttpRequestException ex)
        {
            _logger.LogError($"HTTP Request failed: {ex.Message}");
            throw;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Unexpected error: {ex.Message}");
            throw new Exception("An error occurred while fetching customers.");
        }
    }

    public async Task<List<DetailStatisticLead>> GetDetailMonthlyStatistiqueLeadAsync(int? year)
    {
        try
        {
            string url = "http://localhost:8080/api/crm/customers/leads/monthly-statistic-details";

            if(year.HasValue){
                url += $"?year={year}";
            }

            HttpResponseMessage response = await _httpClient.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError($"API error: {response.StatusCode}");
                throw new HttpRequestException($"API request failed with status code {response.StatusCode}");
            }

            var apiResponse = await response.Content.ReadFromJsonAsync<ApiResponse<List<DetailStatisticLead>>>();
            return apiResponse?.Data ?? [];
        }
        catch (HttpRequestException ex)
        {
            _logger.LogError($"HTTP Request failed: {ex.Message}");
            throw;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Unexpected error: {ex.Message}");
            throw new Exception("An error occurred while fetching customers.");
        }
    }

    public async Task<DetailStatisticLead?> GetDetailMonthlyStatLeadByExpenseIdAsync(int expenseId)
    {
        try
        {
            string url = $"http://localhost:8080/api/crm/customers/leads/monthly-statistic-details/expenses/{expenseId}";

            HttpResponseMessage response = await _httpClient.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError($"API error: {response.StatusCode}");
                throw new HttpRequestException($"API request failed with status code {response.StatusCode}");
            }

            var apiResponse = await response.Content.ReadFromJsonAsync<ApiResponse<DetailStatisticLead>>();
            return apiResponse?.Data ;
        }
        catch (HttpRequestException ex)
        {
            _logger.LogError($"HTTP Request failed: {ex.Message}");
            throw;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Unexpected error: {ex.Message}");
            throw new Exception("An error occurred while fetching customers.");
        }
    }

    public async Task<DetailStatisticLead?> UpdateExpenseAmountAsync(int Id, decimal Amount)
    {
        try
        {
            string url = $"http://localhost:8080/api/crm/expenses/update";
                    // Création de l'objet JSON à envoyer dans le corps de la requête
            var requestBody = new
            {
                amount = Amount,
                expenseId = Id
            };

            // Sérialisation en JSON
            var jsonContent = new StringContent(JsonSerializer.Serialize(requestBody), Encoding.UTF8, "application/json");

            // Envoi de la requête PUT avec le JSON
            HttpResponseMessage response = await _httpClient.PostAsync(url, jsonContent);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError($"API error: {response.StatusCode}");
                throw new HttpRequestException($"API request failed with status code {response.StatusCode}");
            }

            var apiResponse = await response.Content.ReadFromJsonAsync<ApiResponse<DetailStatisticLead>>();
            return apiResponse?.Data;
        }
        catch (HttpRequestException ex)
        {
            _logger.LogError($"HTTP Request failed: {ex.Message}");
            throw;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Unexpected error: {ex.Message}");
            throw new Exception("An error occurred while fetching updating expense");
        }
    }

    public async Task<bool> DeleteLeadAsync(int Id)
    {
        try
        {
            string url = $"http://localhost:8080/api/crm/customers/leads/{Id}";

            // Envoi de la requête PUT avec le JSON
            HttpResponseMessage response = await _httpClient.DeleteAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                _logger.LogError($"API error: {response.StatusCode}");
                throw new HttpRequestException($"API request failed with status code {response.StatusCode}");
            }

            var apiResponse = await response.Content.ReadFromJsonAsync<ApiResponse<bool>>();
            return apiResponse?.Data??false;
        }
        catch (HttpRequestException ex)
        {
            _logger.LogError($"HTTP Request failed: {ex.Message}");
            throw;
        }
        catch (Exception ex)
        {
            _logger.LogError($"Unexpected error: {ex.Message}");
            throw new Exception("An error occurred while fetching updating expense");
        }
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
