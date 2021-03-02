package com.esilv.bankapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Base64;


public class AccountsActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<Account> accounts;
    private List<String> accounts_strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        String account_id = "3";
        ListView list_accounts = (ListView) findViewById(R.id.list_accounts);
        accounts = new ArrayList<>();
        accounts_strings = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, accounts_strings);
        list_accounts.setAdapter(adapter);
        getAccounts();
        Button button = (Button) findViewById(R.id.refresh);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accounts.clear();
                accounts_strings.clear();
                getAccounts();
            }
        });
    }

    public class Account implements java.io.Serializable {
        public int id;
        public String accountName;
        public float amount;
        public String iban;
        public String currency;

        @Override
        public String toString() {
            return accountName + " (" + iban + "): " + amount + " " + currency;
        }
    }

    public void getAccounts() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        byte[] url = getString(R.string.nothing_to_see_here).getBytes();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, new String(Base64.getDecoder().decode(url)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        for (Object v : gson.fromJson(response, List.class)) {
                            accounts_strings.add(gson.fromJson(gson.toJson(v), Account.class).toString());
                            accounts.add(gson.fromJson(gson.toJson(v), Account.class));
                            adapter.notifyDataSetChanged();
                            try {
                                writeOnInternalStorage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "API error, getting accounts from storage", Toast.LENGTH_SHORT)
                                .show();
                        try {
                            readOnInternalStorage();
                            adapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),
                                    "Could not get accounts from storage", Toast.LENGTH_SHORT)
                                    .show();
                        }
                }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void writeOnInternalStorage() throws IOException {
        StorageUtils.setAccountsInStorage(getFilesDir(), this, "accounts", "serialized", this.accounts);
    }

    private void readOnInternalStorage() throws IOException {
        accounts = StorageUtils.getAccountsFromStorage(getFilesDir(), this, "accounts", "serialized");
        accounts_strings.clear();
        for (Account acc : accounts) {
            accounts_strings.add(acc.toString());
        }
    }
}