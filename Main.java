import managers.ExpenseManager;
import managers.CategoryManager;
import managers.ReportGenerator;
import models.Expense;
import models.Budget;
import models.Category;
import utils.DataHandler;
import utils.ValidationHelper;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class - Entry point for Personal Expense Tracker
 * Handles user interaction and menu-driven interface
 */
public class Main {
    private ExpenseManager expenseManager;
    private CategoryManager categoryManager;
    private ReportGenerator reportGenerator;
    private DataHandler dataHandler;
    private Scanner scanner;
    private ArrayList<Budget> budgets;

    /**
     * Constructor - Initialize all managers
     */
    public Main() {
        this.expenseManager = new ExpenseManager();
        this.categoryManager = new CategoryManager();
        this.reportGenerator = new ReportGenerator(expenseManager, categoryManager);
        this.dataHandler = new DataHandler();
        this.scanner = new Scanner(System.in);
        this.budgets = new ArrayList<>();
        
        loadData();
    }

    /**
     * Main method - Application entry point
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    /**
     * Main application loop
     */
    public void run() {
        displayWelcomeMessage();
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    updateExpense();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    viewByCategory();
                    break;
                case 6:
                    manageCategories();
                    break;
                case 7:
                    generateReports();
                    break;
                case 8:
                    manageBudgets();
                    break;
                case 9:
                    exportData();
                    break;
                case 10:
                    viewDashboard();
                    break;
                case 0:
                    running = false;
                    exitApplication();
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
            
            if (running && choice != 0) {
                waitForEnter();
            }
        }
    }

    /**
     * Display welcome message
     */
    private void displayWelcomeMessage() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println(" ".repeat(20) + "PERSONAL EXPENSE TRACKER");
        System.out.println(" ".repeat(25) + "Manage Your Finances");
        System.out.println("=".repeat(80));
        System.out.println();
    }

    /**
     * Display main menu
     */
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(80));
        System.out.println("1.  Add New Expense");
        System.out.println("2.  View All Expenses");
        System.out.println("3.  Update Expense");
        System.out.println("4.  Delete Expense");
        System.out.println("5.  View Expenses by Category");
        System.out.println("6.  Manage Categories");
        System.out.println("7.  Generate Reports");
        System.out.println("8.  Manage Budgets");
        System.out.println("9.  Export Data");
        System.out.println("10. View Dashboard");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(80));
    }

    /**
     * Add new expense
     */
    private void addExpense() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ADD NEW EXPENSE");
        System.out.println("=".repeat(80));
        
        // Get date
        String date = getStringInput("Enter date (DD-MM-YYYY): ");
        if (!ValidationHelper.validateDate(date)) {
            System.out.println("❌ Invalid date format! Please use DD-MM-YYYY.");
            return;
        }
        
        // Get amount
        double amount = getDoubleInput("Enter amount (₹): ");
        if (!ValidationHelper.validateAmount(amount)) {
            System.out.println("❌ Invalid amount! Amount must be positive and less than ₹10,00,000.");
            return;
        }
        
        // Show available categories
        System.out.println("\nAvailable Categories:");
        ArrayList<String> categoryNames = categoryManager.getAllCategoryNames();
        for (int i = 0; i < categoryNames.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categoryNames.get(i));
        }
        
        String category = getStringInput("\nEnter category name: ");
        if (!categoryManager.categoryExists(category)) {
            System.out.println("❌ Category does not exist! Please add it first.");
            return;
        }
        
        String description = getStringInput("Enter description: ");
        if (!ValidationHelper.validateDescription(description)) {
            System.out.println("❌ Description too long! Maximum 200 characters.");
            return;
        }
        
        // Create and add expense
        Expense expense = new Expense(date, amount, category, description);
        if (expenseManager.addExpense(expense)) {
            categoryManager.updateCategoryTotal(category, amount);
            
            // Check budget
            checkBudgetAlert(category, amount);
            
            saveData();
            System.out.println("\n✓ Expense added successfully!");
            System.out.println(expense.toString());
        } else {
            System.out.println("❌ Failed to add expense!");
        }
    }

    /**
     * View all expenses
     */
    private void viewAllExpenses() {
        ArrayList<Expense> expenses = expenseManager.getAllExpenses();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ALL EXPENSES");
        System.out.println("=".repeat(80));
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        
        for (Expense expense : expenses) {
            System.out.println(expense.toString());
        }
        
        System.out.println("=".repeat(80));
        System.out.printf("Total Expenses: %d | Total Amount: ₹%.2f\n", 
            expenses.size(), expenseManager.getTotalExpenses());
        System.out.println("=".repeat(80));
    }

    /**
     * Update existing expense
     */
    private void updateExpense() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UPDATE EXPENSE");
        System.out.println("=".repeat(80));
        
        int id = getIntInput("Enter expense ID to update: ");
        Expense existing = expenseManager.getExpense(id);
        
        if (existing == null) {
            System.out.println("❌ Expense not found with ID: " + id);
            return;
        }
        
        System.out.println("\nCurrent Expense Details:");
        System.out.println(existing.toDetailedString());
        
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        String date = getStringInputOptional("New date (DD-MM-YYYY): ", existing.getDate());
        if (!ValidationHelper.validateDate(date)) {
            System.out.println("❌ Invalid date format!");
            return;
        }
        
        String amountStr = getStringInputOptional("New amount: ", String.valueOf(existing.getAmount()));
        double amount = ValidationHelper.parseDoubleSafe(amountStr, existing.getAmount());
        if (!ValidationHelper.validateAmount(amount)) {
            System.out.println("❌ Invalid amount!");
            return;
        }
        
        String category = getStringInputOptional("New category: ", existing.getCategory());
        if (!categoryManager.categoryExists(category)) {
            System.out.println("❌ Category does not exist!");
            return;
        }
        
        String description = getStringInputOptional("New description: ", existing.getDescription());
        
        // Update category totals
        if (!category.equals(existing.getCategory()) || amount != existing.getAmount()) {
            categoryManager.subtractFromCategoryTotal(existing.getCategory(), existing.getAmount());
            categoryManager.updateCategoryTotal(category, amount);
        }
        
        // Create updated expense
        Expense updated = new Expense(date, amount, category, description);
        if (expenseManager.updateExpense(id, updated)) {
            saveData();
            System.out.println("\n✓ Expense updated successfully!");
        } else {
            System.out.println("❌ Failed to update expense!");
        }
    }

    /**
     * Delete expense
     */
    private void deleteExpense() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("DELETE EXPENSE");
        System.out.println("=".repeat(80));
        
        int id = getIntInput("Enter expense ID to delete: ");
        Expense expense = expenseManager.getExpense(id);
        
        if (expense == null) {
            System.out.println("❌ Expense not found with ID: " + id);
            return;
        }
        
        System.out.println("\nExpense to be deleted:");
        System.out.println(expense.toString());
        
        String confirm = getStringInput("\nAre you sure? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            categoryManager.subtractFromCategoryTotal(expense.getCategory(), expense.getAmount());
            
            if (expenseManager.deleteExpense(id)) {
                saveData();
                System.out.println("\n✓ Expense deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete expense!");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * View expenses by category
     */
    private void viewByCategory() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("VIEW EXPENSES BY CATEGORY");
        System.out.println("=".repeat(80));
        
        ArrayList<String> categoryNames = categoryManager.getAllCategoryNames();
        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categoryNames.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categoryNames.get(i));
        }
        
        String category = getStringInput("\nEnter category name: ");
        ArrayList<Expense> expenses = expenseManager.getExpensesByCategory(category);
        
        if (expenses.isEmpty()) {
            System.out.println("No expenses found in category: " + category);
            return;
        }
        
        System.out.println("\nExpenses in " + category + ":");
        System.out.println("-".repeat(80));
        double total = 0;
        for (Expense expense : expenses) {
            System.out.println(expense.toString());
            total += expense.getAmount();
        }
        System.out.println("=".repeat(80));
        System.out.printf("Total: ₹%.2f\n", total);
    }

    /**
     * Manage categories
     */
    private void manageCategories() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("MANAGE CATEGORIES");
        System.out.println("=".repeat(80));
        System.out.println("1. View All Categories");
        System.out.println("2. Add New Category");
        System.out.println("3. Delete Category");
        System.out.println("0. Back to Main Menu");
        System.out.println("=".repeat(80));
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                categoryManager.displayAllCategories();
                break;
            case 2:
                addCategory();
                break;
            case 3:
                deleteCategory();
                break;
            case 0:
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }

    /**
     * Add new category
     */
    private void addCategory() {
        String name = getStringInput("\nEnter category name: ");
        if (!ValidationHelper.validateCategory(name)) {
            System.out.println("❌ Invalid category name! Must be 2-30 characters.");
            return;
        }
        
        String description = getStringInput("Enter description: ");
        
        if (categoryManager.addCategory(name, description)) {
            saveData();
            System.out.println("\n✓ Category added successfully!");
        } else {
            System.out.println("❌ Category already exists!");
        }
    }

    /**
     * Delete category
     */
    private void deleteCategory() {
        String name = getStringInput("\nEnter category name to delete: ");
        
        if (categoryManager.deleteCategory(name)) {
            saveData();
            System.out.println("\n✓ Category deleted successfully!");
        } else {
            System.out.println("❌ Cannot delete category! It may have expenses or doesn't exist.");
        }
    }

    /**
     * Generate reports
     */
    private void generateReports() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("GENERATE REPORTS");
        System.out.println("=".repeat(80));
        System.out.println("1. Monthly Report");
        System.out.println("2. Category Report");
        System.out.println("3. Budget Report");
        System.out.println("0. Back to Main Menu");
        System.out.println("=".repeat(80));
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                generateMonthlyReport();
                break;
            case 2:
                System.out.println(reportGenerator.generateCategoryReport());
                break;
            case 3:
                System.out.println(reportGenerator.generateBudgetReport(budgets));
                break;
            case 0:
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }

    /**
     * Generate monthly report
     */
    private void generateMonthlyReport() {
        int month = getIntInput("Enter month (1-12): ");
        int year = getIntInput("Enter year: ");
        
        if (!ValidationHelper.validateMonth(month) || !ValidationHelper.validateYear(year)) {
            System.out.println("❌ Invalid month or year!");
            return;
        }
        
        System.out.println(reportGenerator.generateMonthlyReport(month, year));
    }

    /**
     * Manage budgets
     */
    private void manageBudgets() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("MANAGE BUDGETS");
        System.out.println("=".repeat(80));
        System.out.println("1. Set Budget");
        System.out.println("2. View Budget Status");
        System.out.println("0. Back to Main Menu");
        System.out.println("=".repeat(80));
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                setBudget();
                break;
            case 2:
                System.out.println(reportGenerator.generateBudgetReport(budgets));
                break;
            case 0:
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }

    /**
     * Set budget for category
     */
    private void setBudget() {
        ArrayList<String> categoryNames = categoryManager.getAllCategoryNames();
        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categoryNames.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, categoryNames.get(i));
        }
        
        String category = getStringInput("\nEnter category name: ");
        if (!categoryManager.categoryExists(category)) {
            System.out.println("❌ Category does not exist!");
            return;
        }
        
        double limit = getDoubleInput("Enter budget limit (₹): ");
        if (!ValidationHelper.validateAmount(limit)) {
            System.out.println("❌ Invalid amount!");
            return;
        }
        
        // Check if budget already exists for this category
        Budget existingBudget = null;
        for (Budget b : budgets) {
            if (b.getCategory().equalsIgnoreCase(category)) {
                existingBudget = b;
                break;
            }
        }
        
        if (existingBudget != null) {
            existingBudget.setLimit(limit);
            System.out.println("\n✓ Budget updated successfully!");
        } else {
            Budget budget = new Budget(category, limit);
            // Set current spending
            Category cat = categoryManager.getCategory(category);
            if (cat != null) {
                budget.addExpense(cat.getTotalSpent());
            }
            budgets.add(budget);
            System.out.println("\n✓ Budget set successfully!");
        }
        
        // Check if already exceeded
        if (existingBudget != null || budgets.size() > 0) {
            Budget currentBudget = existingBudget != null ? existingBudget : budgets.get(budgets.size() - 1);
            if (currentBudget.isExceeded()) {
                System.out.println("⚠ WARNING: Budget already exceeded!");
            }
        }
    }

    /**
     * Check budget alert for category
     */
    private void checkBudgetAlert(String category, double amount) {
        for (Budget budget : budgets) {
            if (budget.getCategory().equalsIgnoreCase(category)) {
                budget.addExpense(amount);
                
                if (budget.isExceeded()) {
                    System.out.println("\n⚠ ALERT: Budget exceeded for " + category + "!");
                    System.out.printf("Limit: ₹%.2f | Spent: ₹%.2f\n", 
                        budget.getLimit(), budget.getSpent());
                } else if (budget.isNearLimit()) {
                    System.out.println("\n⚠ WARNING: Approaching budget limit for " + category + "!");
                    System.out.printf("Limit: ₹%.2f | Spent: ₹%.2f | Remaining: ₹%.2f\n",
                        budget.getLimit(), budget.getSpent(), budget.getRemainingBudget());
                }
                break;
            }
        }
    }

    /**
     * Export data
     */
    private void exportData() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EXPORT DATA");
        System.out.println("=".repeat(80));
        System.out.println("1. Export to CSV");
        System.out.println("2. Export Category Report to CSV");
        System.out.println("0. Back to Main Menu");
        System.out.println("=".repeat(80));
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                String filename1 = getStringInput("Enter filename (e.g., expenses.csv): ");
                reportGenerator.exportToCSV(filename1);
                break;
            case 2:
                String filename2 = getStringInput("Enter filename (e.g., category_report.csv): ");
                reportGenerator.exportCategoryReportToCSV(filename2);
                break;
            case 0:
                break;
            default:
                System.out.println("❌ Invalid choice!");
        }
    }

    /**
     * View dashboard
     */
    private void viewDashboard() {
        System.out.println(reportGenerator.generateDashboard());
    }

    /**
     * Load data from files
     */
    private void loadData() {
        ArrayList<Expense> loadedExpenses = dataHandler.loadExpenses();
        if (!loadedExpenses.isEmpty()) {
            expenseManager.setExpenses(loadedExpenses);
            System.out.println("✓ Loaded " + loadedExpenses.size() + " expenses from file.");
        }
        
        HashMap<String, Category> loadedCategories = dataHandler.loadCategories();
        if (!loadedCategories.isEmpty()) {
            categoryManager.setCategories(loadedCategories);
            System.out.println("✓ Loaded " + loadedCategories.size() + " categories from file.");
        }
    }

    /**
     * Save data to files
     */
    private void saveData() {
        dataHandler.saveExpenses(expenseManager.getAllExpenses());
        dataHandler.saveCategories(categoryManager.getCategoriesMap());
    }

    /**
     * Exit application
     */
    private void exitApplication() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Saving data...");
        saveData();
        System.out.println("✓ Data saved successfully!");
        System.out.println("\nThank you for using Personal Expense Tracker!");
        System.out.println("=".repeat(80));
        scanner.close();
    }

    // Helper methods for input
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String getStringInputOptional(String prompt, String defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }

    private void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}