package tech.profusion.sumit.securecommunicator.framework.network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.models.ChatMessage;
import tech.profusion.sumit.securecommunicator.framework.entities.Constants;
import tech.profusion.sumit.securecommunicator.framework.listeners.OnMessageRecieved;
import tech.profusion.sumit.securecommunicator.framework.listeners.OnTaskResult;

/**
 * Created by Sumit Agrawal on 13-01-2018.
 */

public class DataExchange {
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    public DataExchange(Context context){
        requestQueue= Volley.newRequestQueue(context);
    }
    public void doLogin(final String contact_no, final String password, final OnTaskResult onTaskResult){
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle=new Bundle();
                        try {
                            Log.i("doLogin Result","Data: "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(!jsonObject.isNull(Constants.AUTH_CODE)){
                                bundle.putString(Constants.AUTH_CODE,jsonObject.getString(Constants.AUTH_CODE));
                            }
                            if(!jsonObject.isNull(Constants.NAME_CODE)){
                                bundle.putString(Constants.NAME_CODE,jsonObject.getString(Constants.NAME_CODE));
                            }
                            if(!jsonObject.isNull(Constants.STATUS_CODE)){
                                bundle.putString(Constants.STATUS_CODE,jsonObject.getString(Constants.STATUS_CODE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onTaskResult.onResult(bundle);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                stringMap.put(Constants.PASSWORD,""+password);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void doRegister(final String contact_no, final String name, final String password, final String sec_question, final String sec_answer, final OnTaskResult onTaskResult){
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle=new Bundle();
                        try {
                            Log.i("doRegister Result","Data: "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(!jsonObject.isNull(Constants.AUTH_CODE)){
                                bundle.putString(Constants.AUTH_CODE,jsonObject.getString(Constants.AUTH_CODE));
                            }
                            if(!jsonObject.isNull(Constants.STATUS_CODE)){
                                bundle.putString(Constants.STATUS_CODE,jsonObject.getString(Constants.STATUS_CODE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onTaskResult.onResult(bundle);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                stringMap.put(Constants.NAME,""+name);
                stringMap.put(Constants.PASSWORD,""+password);
                stringMap.put(Constants.SEC_QUESTION,""+sec_question);
                stringMap.put(Constants.SEC_ANSWER,""+sec_answer);
                Log.i("Data sent",""+contact_no+"\n"+name+"\n"+password+"\n"+sec_question+"\n"+sec_answer);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void doForgotPassword(final String contact_no,final String sec_question, final String sec_answer,final String password, final OnTaskResult onTaskResult){
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.RECOVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle=new Bundle();
                        try {
                            Log.i("doForgotPassword Result","Data: "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(!jsonObject.isNull(Constants.AUTH_CODE)){
                                bundle.putString(Constants.AUTH_CODE,jsonObject.getString(Constants.AUTH_CODE));
                            }
                            if(!jsonObject.isNull(Constants.STATUS_CODE)){
                                bundle.putString(Constants.STATUS_CODE,jsonObject.getString(Constants.STATUS_CODE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onTaskResult.onResult(bundle);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                stringMap.put(Constants.SEC_QUESTION,""+sec_question);
                stringMap.put(Constants.SEC_ANSWER,""+sec_answer);
                stringMap.put(Constants.PASSWORD,""+password);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
        public Bundle sendMessage(final String contact_no,final String auth_code, final String message){
        final Bundle bundle=new Bundle();
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.SEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(!jsonObject.isNull(Constants.STATUS_CODE)){
                                bundle.putString(Constants.STATUS_CODE,jsonObject.getString(Constants.STATUS_CODE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                stringMap.put(Constants.AUTH_CODE,""+auth_code);
                stringMap.put(Constants.MESSAGE,""+message);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
        return bundle;
    }

    public void recieveMessage(final String contact_no,final String auth_code, final String date_time, final OnMessageRecieved onMessageRecieved){
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.RECEIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<ChatMessage> chatMessages=new ArrayList<>();
                        Bundle bundle=new Bundle();
                        try {
                            Log.i("Responce","Data: "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString(Constants.STATUS_CODE);
                            String lastUpdated=jsonObject.getString(Constants.TIMESTAMP);
                            bundle.putString(Constants.STATUS_CODE,status);
                            bundle.putString(Constants.TIMESTAMP,lastUpdated);

                            for(int i=0;i<jsonObject.length()-2;i++){
                                String messageObject=jsonObject.getString(""+i);
                                JSONObject jsonMessage=new JSONObject(messageObject);
                                String contactNo=jsonMessage.getString(Constants.CONTACT_NO);
                                String message=jsonMessage.getString(Constants.MESSAGE);
                                String msgTimestamp=jsonMessage.getString(Constants.TIMESTAMP);
                                ChatMessage chatMessage=new ChatMessage(
                                        message,
                                        Long.parseLong(""+msgTimestamp),
                                        ChatMessage.Type.RECEIVED);
                                chatMessages.add(chatMessage);
                            }

                            onMessageRecieved.onRecieved(chatMessages,bundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("UPDATE","Nothing");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                stringMap.put(Constants.AUTH_CODE,""+auth_code);
                stringMap.put(Constants.TIMESTAMP,""+date_time);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void validateContact(final String contact_no, final OnTaskResult onTaskResult){
        stringRequest=new StringRequest(Request.Method.POST,
                Constants.VALIDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Bundle bundle=new Bundle();
                        try {
                            Log.i("Validate Contact Result","Data: "+response);
                            JSONObject jsonObject=new JSONObject(response);
                            if(!jsonObject.isNull(Constants.NAME_CODE)){
                                bundle.putString(Constants.NAME_CODE,jsonObject.getString(Constants.NAME_CODE));
                            }
                            if(!jsonObject.isNull(Constants.STATUS_CODE)){
                                bundle.putString(Constants.STATUS_CODE,jsonObject.getString(Constants.STATUS_CODE));
                            }
                            onTaskResult.onResult(bundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> stringMap=new HashMap<>();
                stringMap.put(Constants.CONTACT_NO,""+contact_no);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
