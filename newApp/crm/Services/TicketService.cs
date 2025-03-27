using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using crm.Models;
using crm.Models.dto;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Routing;

public class TicketService
{
    private readonly HttpClient _httpClient;
    private readonly ILogger<CustomerService> _logger;

    public TicketService(HttpClient httpClient, ILogger<CustomerService> logger)
    {
        _httpClient = httpClient;
        _logger = logger;
    }


    public async Task<List<StatisticLead>> GetMonthlyStatistiqueTicketAsync(int? year)
    {
        try
        {
            string url = "http://localhost:8080/api/crm/customers/tickets/monthly-statistic";

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

    public async Task<List<DetailStatisticLead>> GetDetailMonthlyStatistiqueTicketAsync(int? year)
    {
        try
        {
            string url = "http://localhost:8080/api/crm/customers/tickets/monthly-statistic-details";

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

    public async Task<DetailStatisticLead?> GetDetailMonthlyStatTicketByExpenseIdAsync(int expenseId)
    {
        try
        {
            string url = $"http://localhost:8080/api/crm/customers/tickets/monthly-statistic-details/expenses/{expenseId}";

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

    public async Task<bool> DeleteTicketAsync(int Id)
    {
        try
        {
            string url = $"http://localhost:8080/api/crm/customers/tickets/{Id}";

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
}
