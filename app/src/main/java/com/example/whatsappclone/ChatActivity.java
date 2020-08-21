package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView chatListView;
    private ArrayList<String> chatList;
    private ArrayAdapter adapter;
    private String selectedUser;
    private EditText chatString;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        selectedUser = getIntent().getStringExtra("selectedUser");
        Toast.makeText(getApplicationContext(), "You are now chatting with " + selectedUser, Toast.LENGTH_SHORT).show();

        chatListView = findViewById(R.id.chat_list_view);
        chatString = findViewById(R.id.edt_chat);
        btnSend = findViewById(R.id.btn_send);
        chatList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatList);
        chatListView.setAdapter(adapter);

        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("chat");

            firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("targetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("sender", selectedUser);
            secondUserChatQuery.whereEqualTo("targetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null)    {
                        for (ParseObject chatObject : objects)    {
                            String waMessage = (String) chatObject.get("message");
                            if (chatObject.get("sender").equals(ParseUser.getCurrentUser().getUsername()))   {
                                waMessage = ParseUser.getCurrentUser().getUsername() + " : " + waMessage;
                            }

                            if (chatObject.get("sender").equals(selectedUser))   {
                                waMessage = selectedUser + " : " + waMessage;

                            }
                            chatList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }   catch (Exception e) {
            e.printStackTrace();
        }




       btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btn_send :
                final String message = chatString.getText().toString();

                ParseObject chat = new ParseObject("chat");
                chat.put("sender", ParseUser.getCurrentUser().getUsername());
                chat.put("targetRecipient", selectedUser);
                chat.put("message", message);
                chat.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)  {
                            Toast.makeText(getApplicationContext(), "message is saved", Toast.LENGTH_SHORT).show();
                            chatList.add(ParseUser.getCurrentUser().getUsername() + " : " + message);
                            adapter.notifyDataSetChanged();
                            chatString.setText("");
                        }
                    }
                });
                break;
        }
    }
}