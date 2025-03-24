namespace crm.Models;
    
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

public class Customer
{
    [Key]
    [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
    [Column("customer_id")]
    public int CustomerId { get; set; }

    [Required(ErrorMessage = "Name is required")]
    [StringLength(100)]  // You can adjust the length as needed
    [Column("name")]
    public string Name { get; set; } = "";

    [Required(ErrorMessage = "Email is required")]
    [EmailAddress(ErrorMessage = "Please enter a valid email format")]
    [Column("email")]
    public string Email { get; set; } = "";

    [StringLength(50)]  // Adjust the length as needed
    [Column("position")]
    public string? Position { get; set; }

    [StringLength(20)]  // Adjust the length as needed
    [Column("phone")]
    public string? Phone { get; set; }

    [StringLength(250)]  // Adjust the length as needed
    [Column("address")]
    public string? Address { get; set; }

    [StringLength(100)]  // Adjust the length as needed
    [Column("city")]
    public string? City { get; set; }

    [StringLength(50)]  // Adjust the length as needed
    [Column("state")]
    public string? State { get; set; }

    [Required(ErrorMessage = "Country is required")]
    [StringLength(50)]  // Adjust the length as needed
    [Column("country")]
    public string? Country { get; set; }

    [StringLength(500)]  // Adjust the length as needed
    [Column("description")]
    public string? Description { get; set; }

    [StringLength(100)]  // Adjust the length as needed
    [Column("twitter")]
    public string? Twitter { get; set; }

    [StringLength(100)]  // Adjust the length as needed
    [Column("facebook")]
    public string? Facebook { get; set; }

    [StringLength(100)]  // Adjust the length as needed
    [Column("youtube")]
    public string? Youtube { get; set; }

    [ForeignKey("UserId")]
    [Column("user_id")]
    public int UserId { get; set; }

    [ForeignKey("ProfileId")]
    [Column("profile_id")]
    public int? ProfileId { get; set; }

    [Column("created_at")]
    public DateTime CreatedAt { get; set; }
}