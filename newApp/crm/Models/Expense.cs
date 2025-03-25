namespace crm.Models;

using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


public class Expense
{
    [Column("expense_id")]
    public int Id { get; set; }

    [Required(ErrorMessage = "Amount is required")]
    [Range(0.00, 9999999.99, ErrorMessage = "Amount must be between 0.00 and 9999999.99")]
    public decimal Amount { get; set; }

    [Required]
    public DateTime ExpenseDate { get; set; }
}
