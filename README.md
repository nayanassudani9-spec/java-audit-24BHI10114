# Personal Expense Tracker

## Overview
A Java-based console application for tracking personal expenses, managing budgets, and generating financial reports. This project demonstrates object-oriented programming principles, file handling, and data management concepts.

## Features
✅ **Expense Management**
- Add new expense entries with date, amount, category, and description
- View all expenses with filtering options
- Update existing expense records
- Delete expense entries

✅ **Category Management**
- Create custom expense categories
- View all categories with expense totals
- Manage category budgets

✅ **Report Generation**
- Monthly expense summaries
- Category-wise expense breakdown
- Budget vs actual spending analysis
- Expense trends and insights

✅ **Data Persistence**
- Automatic save to JSON file
- Load previous data on startup
- Export reports to CSV format

## Technologies Used
- **Java 8+**: Core programming language
- **JSON**: Data storage format using Gson library
- **File I/O**: For data persistence
- **Collections Framework**: ArrayList, HashMap for data management
- **Exception Handling**: Robust error management

## System Requirements
- Java Development Kit (JDK) 8 or higher
- Gson library (included in lib/ folder)
- Any text editor or IDE (Eclipse, IntelliJ IDEA, VS Code)

## Installation & Setup

### Step 1: Clone the Repository
```bash
git clone https://github.com/yourusername/personal-expense-tracker.git
cd personal-expense-tracker
```

### Step 2: Compile the Project
```bash
# Navigate to src directory
cd src

# Compile all Java files
javac -d ../bin -cp "../lib/*:." models/*.java managers/*.java utils/*.java Main.java
```

### Step 3: Run the Application
```bash
# From project root directory
java -cp "bin:lib/*" Main
```

## Usage

### Main Menu Options
1. **Add Expense**: Enter date, amount, category, and description
2. **View All Expenses**: Display all recorded expenses
3. **Update Expense**: Modify existing expense by ID
4. **Delete Expense**: Remove expense by ID
5. **View by Category**: Filter expenses by category
6. **Generate Report**: Create monthly or category-wise reports
7. **Set Budget**: Define budget limits for categories
8. **Export Data**: Save reports to CSV file
9. **Exit**: Save and close application

### Example Usage
```
Welcome to Personal Expense Tracker
===================================
1. Add Expense
2. View All Expenses
3. Update Expense
...

Enter your choice: 1

Enter date (DD-MM-YYYY): 15-11-2024
Enter amount: 500
Enter category: Food
Enter description: Lunch at restaurant

✓ Expense added successfully!
```

## Project Structure
```
personal-expense-tracker/
├── src/
│   ├── models/              # Data models
│   │   ├── Expense.java
│   │   ├── Category.java
│   │   └── Budget.java
│   ├── managers/            # Business logic
│   │   ├── ExpenseManager.java
│   │   ├── CategoryManager.java
│   │   └── ReportGenerator.java
│   ├── utils/               # Helper classes
│   │   ├── DataHandler.java
│   │   └── ValidationHelper.java
│   └── Main.java            # Entry point
├── data/
│   └── expenses.json        # Data storage
├── docs/                    # Documentation
└── lib/                     # External libraries
```

## Non-Functional Requirements Implemented

### 1. Performance
- Efficient data retrieval using HashMap
- O(1) lookup for expense by ID
- Optimized report generation

### 2. Security
- Input validation to prevent invalid data
- Exception handling for file operations
- Data integrity checks

### 3. Usability
- Clear menu-driven interface
- Helpful error messages
- Intuitive workflow

### 4. Reliability
- Automatic data backup before operations
- Transaction rollback on errors
- Data consistency validation

### 5. Maintainability
- Modular code structure
- Clear separation of concerns
- Comprehensive comments
- Follows Java naming conventions

### 6. Error Handling
- Try-catch blocks for all I/O operations
- Custom exception messages
- Graceful failure handling

## Testing

### Manual Testing Checklist
- [ ] Add expense with valid data
- [ ] Add expense with invalid date format
- [ ] View expenses when list is empty
- [ ] Update non-existent expense
- [ ] Delete expense and verify removal
- [ ] Generate report with no data
- [ ] Set budget and check alerts
- [ ] Application startup with corrupted data file

### Running Tests
```bash
cd tests
javac -cp "../src:../lib/*:." ExpenseTest.java
java -cp ".:../src:../lib/*" org.junit.runner.JUnitCore ExpenseTest
```

## Screenshots
(Add screenshots after implementation)
- Main Menu
- Add Expense Screen
- Expense List View
- Monthly Report
- Budget Alert

## Future Enhancements
- Graphical User Interface (GUI) using JavaFX
- Database integration (MySQL/PostgreSQL)
- Multi-user support with authentication
- Cloud sync functionality
- Mobile app companion
- Advanced analytics with charts
- Receipt image attachment
- Recurring expense automation

## Challenges Faced
- JSON parsing and serialization
- Date format validation
- File handling edge cases
- Menu-driven flow control

## Key Learnings
- Object-oriented design principles
- File I/O operations in Java
- Exception handling strategies
- Collections framework usage
- JSON data management

## Contributing
This is an academic project. Suggestions and feedback are welcome!

## License
Academic Project - VIT University

## Author
[Your Name]
[Your Registration Number]
[Your Email]

## Acknowledgments
- VIT University for project guidelines
- Java documentation and community
- Gson library by Google