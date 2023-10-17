package com.example.expenseexplorer.views.activites;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expenseexplorer.R;
import com.example.expenseexplorer.adapters.TransactionsAdapter;
import com.example.expenseexplorer.databinding.ActivityMainBinding;
import com.example.expenseexplorer.models.Transaction;
import com.example.expenseexplorer.utils.Constants;
import com.example.expenseexplorer.utils.Helper;
import com.example.expenseexplorer.viewmodels.MainViewModel;
import com.example.expenseexplorer.views.fragments.AddTransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    Calendar calendar;
    /*
    0 = Daily
    1 = Monthly
    2 = Calendar
    3 = Summary
    4 = Notes
     */

    BottomNavigationItemView stats;
    BottomNavigationItemView accounts;


    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        PackageManager packageManager = getPackageManager();
//        Intent launchIntent = packageManager.getLaunchIntentForPackage("com.example.expenseexplorer");
//
//        if (launchIntent != null) {
//            String targetActivity = launchIntent.getComponent().getClassName();
//            String targetPackage = launchIntent.getComponent().getPackageName();
//            Toast.makeText(MainActivity.this, targetActivity, Toast.LENGTH_SHORT).show();
//            Toast.makeText(MainActivity.this, targetPackage, Toast.LENGTH_SHORT).show();
//
//            // Now you have the package name and activity name
//            // Do something with targetActivity and targetPackage
//        }

//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.example.firebaseauthentication", "com.example.firebaseauthentication.SignupActivity"));
//        startActivity(intent);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        Button profileButton = findViewById(R.id.profileBtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "hohoho", Toast.LENGTH_SHORT).show();

                // Start ProfileActivity and pass the data to it
                Intent profileIntent = new Intent();
                profileIntent.setComponent(new ComponentName("com.example.firebaseauthentication", "com.example.firebaseauthentication.MainActivity"));
                profileIntent.putExtra("name", name);
                profileIntent.putExtra("email", email);
                profileIntent.putExtra("username", username);
                profileIntent.putExtra("password", password);
                startActivity(profileIntent);
            }
        });

        stats = findViewById(R.id.stats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryintent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(categoryintent);
                Toast.makeText(MainActivity.this, "stats", Toast.LENGTH_SHORT).show();
            }
        });


        accounts = findViewById(R.id.accounts);
        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountintent = new Intent(MainActivity.this, AccountsActivity.class);
                startActivity(accountintent);
                Toast.makeText(MainActivity.this, "accounts", Toast.LENGTH_SHORT).show();
            }
        });


        viewModel = new ViewModelProvider(this).get(MainViewModel.class);




        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c-> {
            if(Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });


        binding.floatingActionButton.setOnClickListener(c -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });


        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                } else if(tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
                else if(tab.getText().equals("Notes")) {
                    Constants.SELECTED_TAB = 4;
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.notes", "com.example.notes.MainActivity"));
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter transactionsAdapter = new TransactionsAdapter(MainActivity.this, transactions);
                binding.transactionsList.setAdapter(transactionsAdapter);
                if(transactions.size() > 0) {
                    binding.emptyState.setVisibility(View.GONE);
                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);



    }

    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }



    void updateDate() {
        if(Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        } else if(Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }



}