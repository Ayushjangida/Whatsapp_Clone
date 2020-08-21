package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView usersListView;
    private ArrayAdapter arrayAdapter;
    private List<String> usersList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        usersListView = findViewById(R.id.list_view_user_activity);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_user_activity);
        usersListView.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", usersList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if (objects.size() > 0 && e == null)    {
                                for(ParseUser user : objects)   {
                                    usersList.add(user.getUsername());
                                }
                                arrayAdapter.notifyDataSetChanged();
                                if (swipeRefreshLayout.isRefreshing())  swipeRefreshLayout.setRefreshing(false);
                            }   else    {
                                if (swipeRefreshLayout.isRefreshing())  swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }   catch (Exception e) {

                }
            }
        });

        getUsers();
    }

    private void getUsers() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Users...");
            progressDialog.show();
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null)    {
                        for(ParseUser user : objects)   {
                            usersList.add(user.getUsername());
                        }
                        progressDialog.dismiss();
                        usersListView.setAdapter(arrayAdapter);
                    }
                }
            });
        }   catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout_user)   {
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(UserActivity.this, ChatActivity.class);
        intent.putExtra("selectedUser", usersList.get(position));
        startActivity(intent);

    }
}