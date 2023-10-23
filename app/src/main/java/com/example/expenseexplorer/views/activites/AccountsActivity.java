package com.example.expenseexplorer.views.activites;

import android.content.Context;
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
import com.example.expenseexplorer.adapters.AccountsAdapter;
import com.example.expenseexplorer.models.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AccountsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AccountsAdapter accountsAdapter;
    private ArrayList<Account> accounts;
    private EditText etCustomAccountName;
    private Button btnAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        recyclerView = findViewById(R.id.recyclerViewAccounts);
        etCustomAccountName = findViewById(R.id.etCustomAccountName);
        btnAddAccount = findViewById(R.id.btnAddAccount);

        // Load accounts from SharedPreferences
        accounts = loadAccountsFromSharedPreferences();

        // Initialize and set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountsAdapter = new AccountsAdapter(this, accounts, new AccountsAdapter.AccountsClickListener() {
            @Override
            public void onAccountSelected(Account account) {
                // Handle account item click (e.g., edit or perform actions related to the selected account)
            }

            @Override
            public void onAccountLongClicked(Account account) {
                // Handle long press on an account item to delete it
                removeAccount(account);
            }
        });
        recyclerView.setAdapter(accountsAdapter);

        // Add Account Button Click Listener
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the custom account name entered by the user
                String customAccountName = etCustomAccountName.getText().toString().trim();
                if (!customAccountName.isEmpty()) {
                    // Create a new account with the custom name and an initial amount
                    Account newAccount = new Account(0, customAccountName);

                    // Add the new account to the local 'accounts' list and update the RecyclerView
                    accounts.add(newAccount);
                    accountsAdapter.notifyDataSetChanged();

                    // Save the updated 'accounts' list to SharedPreferences
                    saveAccountsToSharedPreferences(accounts);

                    // Clear the input field
                    etCustomAccountName.setText("");
                }
            }
        });
    }

    private void saveAccountsToSharedPreferences(ArrayList<Account> accounts) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the accounts list to a JSON string and save it
        Gson gson = new Gson();
        String accountsJson = gson.toJson(accounts);
        editor.putString("accounts", accountsJson);
        editor.apply();
    }

    private ArrayList<Account> loadAccountsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountsJson = sharedPreferences.getString("accounts", null);

        if (accountsJson != null) {
            // Convert the JSON string back to an ArrayList
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Account>>() {}.getType();
            return gson.fromJson(accountsJson, type);
        } else {
            return new ArrayList<>(); // Return an empty list if no accounts are saved
        }
    }

    private void removeAccount(Account account) {
        accounts.remove(account);
        accountsAdapter.notifyDataSetChanged();

        // Remove the account from SharedPreferences
        saveAccountsToSharedPreferences(accounts);

        Toast.makeText(this, "Account removed", Toast.LENGTH_SHORT).show();
    }
}
