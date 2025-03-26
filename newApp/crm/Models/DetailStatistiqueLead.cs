namespace crm.Models;

public class DetailStatisticLead
{
    public int LeadId { get; set;}
    public string Name { get; set;} = "";
    public string Email { get; set;} = "";
    public int ExpenseId { get; set; }
    public decimal Amount { get; set; }
}