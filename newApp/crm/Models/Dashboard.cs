namespace crm.Models;

public class Dashboard
{
    public int? BudgetId {get; set; }
    public double TotalBudget {get; set; }
    public double TotalExpense {get; set; }
    public double TotalRest {get; set; }
    public String? CustomerName {get; set; }
    public int? CustomerId {get; set; }
}