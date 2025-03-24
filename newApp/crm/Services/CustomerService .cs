using System.Collections.Generic;
using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using crm.Models;

public class CustomerService
{
    private readonly HttpClient _httpClient;

    public CustomerService(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<List<Customer>> GetCustomersAsync()
    {
        var response = await _httpClient.GetAsync("http://localhost:8080/api/crm/customers");
        response.EnsureSuccessStatusCode();

        var json = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<List<Customer>>(json, options: new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
    }
}
