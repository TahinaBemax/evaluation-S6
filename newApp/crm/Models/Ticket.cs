namespace crm.Models;
    
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class Ticket
{
    [Column("ticket_id")]
    public int TicketId { get; set; }

    [Required(ErrorMessage = "Subject is required")]
    public string Subject { get; set; } = string.Empty;

    public string? Description { get; set; }

    [Required(ErrorMessage = "Status is required")]
    [RegularExpression("^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived)$",
        ErrorMessage = "Invalid status")]
    public string Status { get; set; } = string.Empty;

    [Required(ErrorMessage = "Priority is required")]
    [RegularExpression("^(low|medium|high|closed|urgent|critical)$", ErrorMessage = "Invalid priority")]
    public string Priority { get; set; } = string.Empty;

    public int? ManagerId { get; set; }  

    public virtual List<Expense>? Expenses { get; set; }

    [ForeignKey("EmployeeId")]
    [Column("employee_id")]
    public int? EmployeeId { get; set; }  
    public int? CustomerId { get; set; }  
    public virtual Customer? Customer { get; set; }

    [Column("created_at")]
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}