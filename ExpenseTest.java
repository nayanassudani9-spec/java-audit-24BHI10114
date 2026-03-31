package tests;

// Place this in tests/ directory

import models.Expense;
import models.Category;
import managers.ExpenseManager;
import managers.CategoryManager;
import utils.ValidationHelper;

/**
 * Simple test class to verify core functionality
 * Run this to test basic operations
 */
public class ExpenseTest {
    
    public static void main(String[] args) {
        System.out.println("Running Personal Expense Tracker Tests...\n");
        
        testExpenseCreation();
        testCategoryManagement();
        testExpenseManager();
        testValidation();
        
        System.out.println("\n✓ All tests completed!");
    }
    
    /**
     * Test Expense model creation
     */
    private static void testExpenseCreation() {
        System.out.println("Test 1: Expense Creation");
        System.out.println("-".repeat(50));
        
        Expense expense = new Expense("15-11-2024", 500.0, "Food", "Lunch");
        expense.setExpenseId(1);
        
        assert expense.getAmount() == 500.0 : "Amount should be 500.0";
        assert expense.getCategory().equals("Food") : "Category should be Food";
        assert expense.getDescription().equals("Lunch") : "Description should be Lunch";
        
        System.out.println(expense.toString());
        System.out.println("✓ Expense creation test passed!\n");
    }
    
    /**
     * Test Category management
     */
    private static void testCategoryManagement() {
        System.out.println("Test 2: Category Management");
        System.out.println("-".repeat(50));
        
        CategoryManager manager = new CategoryManager();
        
        // Test default categories
        assert manager.categoryExists("Food") : "Food category should exist";
        assert manager.categoryExists("Transport") : "Transport category should exist";
        
        // Test adding new category
        boolean added = manager.addCategory("Gifts", "Gift expenses");
        assert added : "Should be able to add new category";
        assert manager.categoryExists("Gifts") : "Gifts category should exist";
        
        // Test duplicate category
        boolean duplicate = manager.addCategory("Gifts", "Duplicate");
        assert !duplicate : "Should not add duplicate category";
        
        System.out.println("Total categories: " + manager.getCategoryCount());
        System.out.println("✓ Category management test passed!\n");
    }
    
    /**
     * Test ExpenseManager operations
     */
    private static void testExpenseManager() {
        System.out.println("Test 3: Expense Manager Operations");
        System.out.println("-".repeat(50));
        
        ExpenseManager manager = new ExpenseManager();
        
        // Add expenses
        Expense e1 = new Expense("15-11-2024", 500.0, "Food", "Lunch");
        Expense e2 = new Expense("16-11-2024", 300.0, "Transport", "Bus fare");
        Expense e3 = new Expense("17-11-2024", 1000.0, "Food", "Dinner");
        
        manager.addExpense(e1);
        manager.addExpense(e2);
        manager.addExpense(e3);
        
        // Test total
        double total = manager.getTotalExpenses();
        assert total == 1800.0 : "Total should be 1800.0";
        
        // Test count
        assert manager.getExpenseCount() == 3 : "Should have 3 expenses";
        
        // Test filter by category
        var foodExpenses = manager.getExpensesByCategory("Food");
        assert foodExpenses.size() == 2 : "Should have 2 food expenses";
        
        // Test get by ID
        Expense retrieved = manager.getExpense(1);
        assert retrieved != null : "Should retrieve expense with ID 1";
        assert retrieved.getAmount() == 500.0 : "Retrieved expense amount should be 500";
        
        // Test update
        Expense updated = new Expense("15-11-2024", 600.0, "Food", "Updated lunch");
        boolean updateSuccess = manager.updateExpense(1, updated);
        assert updateSuccess : "Update should succeed";
        assert manager.getExpense(1).getAmount() == 600.0 : "Amount should be updated to 600";
        
        // Test delete
        boolean deleteSuccess = manager.deleteExpense(2);
        assert deleteSuccess : "Delete should succeed";
        assert manager.getExpenseCount() == 2 : "Should have 2 expenses after delete";
        
        System.out.println("Total expenses: " + manager.getExpenseCount());
        System.out.println("Total amount: ₹" + manager.getTotalExpenses());
        System.out.println("✓ Expense manager test passed!\n");
    }
    
    /**
     * Test validation utilities
     */
    private static void testValidation() {
        System.out.println("Test 4: Validation Utilities");
        System.out.println("-".repeat(50));
        
        // Test date validation
        assert ValidationHelper.validateDate("15-11-2024") : "Valid date should pass";
        assert !ValidationHelper.validateDate("32-11-2024") : "Invalid day should fail";
        assert !ValidationHelper.validateDate("15-13-2024") : "Invalid month should fail";
        assert !ValidationHelper.validateDate("15/11/2024") : "Wrong format should fail";
        
        // Test amount validation
        assert ValidationHelper.validateAmount(100.0) : "Valid amount should pass";
        assert !ValidationHelper.validateAmount(-50.0) : "Negative amount should fail";
        assert !ValidationHelper.validateAmount(0.0) : "Zero amount should fail";
        
        // Test category validation
        assert ValidationHelper.validateCategory("Food") : "Valid category should pass";
        assert !ValidationHelper.validateCategory("") : "Empty category should fail";
        assert !ValidationHelper.validateCategory("A") : "Too short category should fail";
        
        // Test numeric validation
        assert ValidationHelper.isNumeric("123.45") : "Should recognize number";
        assert !ValidationHelper.isNumeric("abc") : "Should reject non-number";
        
        // Test integer validation
        assert ValidationHelper.isInteger("123") : "Should recognize integer";
        assert !ValidationHelper.isInteger("123.45") : "Should reject decimal as integer";
        
        System.out.println("✓ Validation test passed!\n");
    }
}