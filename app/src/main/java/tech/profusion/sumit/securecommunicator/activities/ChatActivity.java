package tech.profusion.sumit.securecommunicator.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import tech.profusion.sumit.securecommunicator.R;
import tech.profusion.sumit.securecommunicator.framework.adapters.PreferenceAdapter;
import tech.profusion.sumit.securecommunicator.framework.entities.Constants;
import tech.profusion.sumit.securecommunicator.framework.listeners.OnMessageRecieved;
import tech.profusion.sumit.securecommunicator.framework.network.DataExchange;

public class ChatActivity extends AppCompatActivity {
    private DataExchange dataExchange;
    private Handler handler;
    private Runnable runnable;

    private String auth_code="",contact_no="",name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle=getIntent().getExtras();
        auth_code=bundle.getString(Constants.AUTH_CODE);
        contact_no=bundle.getString(Constants.TARGET_CONTACT_NO);
        name=bundle.getString(Constants.NAME);

        dataExchange=new DataExchange(this);
        final PreferenceAdapter preferenceAdapter=new PreferenceAdapter(this,Constants.AUTH_PREFERENCE);
        final ChatView chatView = findViewById(R.id.chat_view);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                dataExchange.recieveMessage(
                        contact_no,
                        auth_code,
                        "0"+preferenceAdapter.getStringPreference(contact_no+Constants.TIMESTAMP),
                        new OnMessageRecieved() {
                            @Override
                            public void onRecieved(ArrayList<ChatMessage> chatMessages, Bundle bundle) {
                                if(bundle.getString(Constants.STATUS_CODE).equals("701")) {
                                    Log.i("Data", "Messages: " + bundle.getString(Constants.MESSAGE));
                                    if(bundle.getString(Constants.STATUS_CODE).equals(Constants.STATUS_OK)){
                                        chatView.addMessages(chatMessages);
                                        preferenceAdapter.putStringPreference(contact_no+Constants.TIMESTAMP,bundle.getString(Constants.TIMESTAMP));
                                    }else{
                                        Log.i("Error", "STATUS CODE: " + bundle.getString(Constants.STATUS_CODE));
                                    }
                                }
                            }
                        });

                handler.postDelayed(runnable,3000);
            }
        };
        handler.post(runnable);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                dataExchange.sendMessage(
                        contact_no,
                        auth_code,
                        chatMessage.getMessage());
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        finish();
    }
}
