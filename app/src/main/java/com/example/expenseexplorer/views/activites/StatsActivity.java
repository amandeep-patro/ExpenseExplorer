package com.example.expenseexplorer.views.activites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expenseexplorer.R;
import com.example.expenseexplorer.adapters.CategoryAdapter;
import com.example.expenseexplorer.models.Category;
import com.example.expenseexplorer.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories;
    private EditText etCustomCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        etCustomCategoryName = findViewById(R.id.etCustomCategoryName);

        // Load categories from SharedPreferences
        categories = loadCategoriesFromSharedPreferences();

        // Initialize and set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, categories, new CategoryAdapter.CategoryClickListener() {
            @Override
            public void onCategoryClicked(Category category) {
                // Handle category item click (e.g., perform actions related to the selected category)
            }

            @Override
            public void onCategoryLongClicked(Category category) {
                // Handle long press on a category item to delete it
                removeCategory(category);
            }
        });
        recyclerView.setAdapter(categoryAdapter);

        // Add Category Button Click Listener (to add a new category with a custom name)
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the custom category name entered by the user
                String customCategoryName = etCustomCategoryName.getText().toString().trim();
                if (!customCategoryName.isEmpty()) {
                    // Create a new category with the custom name and default values for image and color
                    Category newCategory = new Category(customCategoryName, R.drawable.ic_commonpayment, R.color.main_yellow2);

                    // Add the new category to the local 'categories' list and update the RecyclerView
                    categories.add(newCategory);
                    categoryAdapter.notifyDataSetChanged();

                    // Save the updated 'categories' list to SharedPreferences
                    saveCategoriesToSharedPreferences(categories);
                    // Add the new category to Constants for permanent storage
                    Constants.addCategory(newCategory);

                    // Clear the input field
                    etCustomCategoryName.setText("");
                }
            }
        });
    }

    private void saveCategoriesToSharedPreferences(ArrayList<Category> categories) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the categories list to a JSON string and save it
        Gson gson = new Gson();
        String categoriesJson = gson.toJson(categories);
        editor.putString("categories", categoriesJson);
        editor.apply();
    }

    private ArrayList<Category> loadCategoriesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String categoriesJson = sharedPreferences.getString("categories", null);

        if (categoriesJson != null) {
            // Convert the JSON string back to an ArrayList
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Category>>() {}.getType();
            return gson.fromJson(categoriesJson, type);
        } else {
            return new ArrayList<>(); // Return an empty list if no categories are saved
        }
    }

    private void removeCategory(Category category) {
        categories.remove(category);
        categoryAdapter.notifyDataSetChanged();

        // Remove the category from SharedPreferences
        saveCategoriesToSharedPreferences(categories);

        // Optionally, you can remove the category from Constants for permanent storage
        Constants.removeCategory(category);

        Toast.makeText(this, "Category removed", Toast.LENGTH_SHORT).show();
    }
}
