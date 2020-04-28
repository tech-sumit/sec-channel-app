package tech.profusion.sumit.securecommunicator.framework.listeners;

import android.os.Bundle;

import java.util.ArrayList;

import co.intentservice.chatui.models.ChatMessage;

/**
 * Created by Sumit Agrawal on 31-01-2018.
 */

public interface OnMessageRecieved {
    void onRecieved(ArrayList<ChatMessage> chatMessages, Bundle bundle);
}
