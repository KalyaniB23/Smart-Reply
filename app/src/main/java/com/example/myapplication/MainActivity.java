package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText etSendMessage;
    Button btnSendMessage;
    TextView tvReplyMessage;

    List<TextMessage> conversion;
    String userUID ="123456";
    SmartReplyGenerator smartReplyGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSendMessage=findViewById(R.id.etSendMessage);
        btnSendMessage=findViewById(R.id.btnSendMessage);
        tvReplyMessage=findViewById(R.id.tvReplyMessage);

        conversion=new ArrayList<>();
        smartReplyGenerator= SmartReply.getClient();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=etSendMessage.getText().toString().trim();
                conversion.add(TextMessage.createForRemoteUser(message,System.currentTimeMillis(), userUID));

                smartReplyGenerator.suggestReplies(conversion).addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult smartReplySuggestionResult) {
                        if(smartReplySuggestionResult.getStatus()==SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE){
                            tvReplyMessage.setText("No Reply");
                        }else if (smartReplySuggestionResult.getStatus()==SmartReplySuggestionResult.STATUS_SUCCESS){
                            String reply = "";

                            for(SmartReplySuggestion suggestion:smartReplySuggestionResult.getSuggestions()){
                                reply= reply + suggestion.getText()+"\n ";

                            }

                            tvReplyMessage.setText(reply);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvReplyMessage.setText("Error : " +e.getMessage());

                    }
                });
            }
        });


    }
}