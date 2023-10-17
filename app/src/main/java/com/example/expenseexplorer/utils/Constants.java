package com.example.expenseexplorer.utils;

import com.example.expenseexplorer.R;
import com.example.expenseexplorer.models.Category;

import java.util.ArrayList;

public class Constants {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static ArrayList<Category> categories;

    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;

    public static int SELECTED_TAB = 0;

    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.ic_commonpayment, R.color.main_yellow2));
        categories.add(new Category("Stationary", R.drawable.ic_commonpayment, R.color.main_yellow2));
        categories.add(new Category("Investment", R.drawable.ic_commonpayment, R.color.main_yellow2));
        categories.add(new Category("Food", R.drawable.ic_commonpayment, R.color.main_yellow2));
        categories.add(new Category("Rent", R.drawable.ic_commonpayment, R.color.main_yellow2));
        categories.add(new Category("Other", R.drawable.ic_commonpayment, R.color.main_yellow2));
    }

    // Add a new category
    public static void addCategory(Category category) {
        categories.add(category);
    }

    public static void removeCategory(Category category) {
        categories.remove(category);
    }

    // Remove a category by name
    public static void removeCategory(String categoryName) {
        Category categoryToRemove = null;
        for (Category category : categories) {
            if (category.getCategoryName().equals(categoryName)) {
                categoryToRemove = category;
                break;
            }
        }
        if (categoryToRemove != null) {
            categories.remove(categoryToRemove);
        }
    }

    public static Category getCategoryDetails(String categoryName) {
        for (Category cat :
                categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public static int getAccountsColor(String accountName) {
        switch (accountName) {
            case "Online":
                return R.color.category4;
            case "Cash":
                return R.color.category4;
            default:
                return R.color.category4;
        }
    }

}
