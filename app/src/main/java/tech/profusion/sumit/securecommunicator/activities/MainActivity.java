package tech.profusion.sumit.securecommunicator.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;
import tech.profusion.sumit.securecommunicator.R;
import tech.profusion.sumit.securecommunicator.framework.adapters.ContactListAdapter;
import tech.profusion.sumit.securecommunicator.framework.adapters.DBHandler;
import tech.profusion.sumit.securecommunicator.framework.adapters.PreferenceAdapter;
import tech.profusion.sumit.securecommunicator.framework.entities.Constants;
import tech.profusion.sumit.securecommunicator.framework.entities.Contact;
import tech.profusion.sumit.securecommunicator.framework.listeners.OnTaskResult;
import tech.profusion.sumit.securecommunicator.framework.network.DataExchange;

public class MainActivity extends AppCompatActivity {

    private String sec_question="";
    private PreferenceAdapter preferenceAdapter;
    private ListView contactsListView;
    private ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsListView=findViewById(R.id.contactsListView);
        setContactsListViewAdapter();
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact= (Contact) contactListAdapter.getItem(position);
                String contactNo=contact.getContactNo();
                String contactName=contact.getName();
                PreferenceAdapter preferenceAdapter=new PreferenceAdapter(MainActivity.this,Constants.AUTH_PREFERENCE);
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.AUTH_CODE,preferenceAdapter.getStringPreference(Constants.AUTH_CODE));
                bundle.putString(Constants.NAME,contactName);
                bundle.putString(Constants.TARGET_CONTACT_NO,contactNo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.addContactFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=View.inflate(MainActivity.this,R.layout.chat_add_layout,null);
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(view);
                dialog.show();
                dialog.findViewById(R.id.startChatButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText textStartNewChatContactNo=dialog.findViewById(R.id.textStartNewChatContactNo);
                        DataExchange dataExchange=new DataExchange(MainActivity.this);
                        dataExchange.validateContact(textStartNewChatContactNo.getText().toString(), new OnTaskResult() {
                            @Override
                            public void onResult(Bundle bundle) {
                                String statusCode=bundle.getString(Constants.STATUS_CODE);
                                if(statusCode.equals("701")) {
                                    String name = bundle.getString(Constants.NAME_CODE);
                                    Contact contact=new Contact(name,textStartNewChatContactNo.getText().toString(),"");
                                    DBHandler dbHandler=new DBHandler(MainActivity.this);
                                    dbHandler.addContact(contact);
                                    setContactsListViewAdapter();
                                }else{
                                    Toast.makeText(MainActivity.this,
                                            "Invalid contact number",
                                            Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();}
                        });
                    }
                });
            }
        });
        preferenceAdapter=new PreferenceAdapter(this, Constants.AUTH_PREFERENCE);
        if(!preferenceAdapter.checkLogin()){
            showLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout:
                preferenceAdapter.setLogin(false);
                DBHandler dbHandler=new DBHandler(MainActivity.this);
                dbHandler.deleteAll();
                showLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void setContactsListViewAdapter(){
        contactListAdapter=new ContactListAdapter(
                MainActivity.this,
                new DBHandler(MainActivity.this).getAllContacts());
        contactsListView.setAdapter(contactListAdapter);
    }

    private void showLogin() {
        final View view=View.inflate(this,R.layout.login_dialog_layout,null);
        final AlertDialog dialog= new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .show();

        Button login=view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText textContactNo=view.findViewById(R.id.textContactNo);
                final EditText textPassword=view.findViewById(R.id.textPassword);

                DataExchange dataExchange=new DataExchange(MainActivity.this);
                dataExchange.doLogin(textContactNo.getText().toString(),
                        textPassword.getText().toString(),
                        new OnTaskResult() {
                    @Override
                    public void onResult(Bundle bundle) {
                        String status_code= bundle.getString(Constants.STATUS_CODE);
                        if (status_code != null) {
                            switch (status_code) {
                                case Constants.STATUS_OK:
                                    preferenceAdapter.putStringPreference(
                                            Constants.AUTH_CODE,
                                            bundle.getString(Constants.AUTH_CODE));
                                    preferenceAdapter.setLogin(true);
                                    preferenceAdapter.putSelfContact(
                                            bundle.getString(Constants.NAME_CODE),
                                            textContactNo.getText().toString(),
                                            textPassword.getText().toString());
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Login Successful, Welcome "+bundle
                                                    .getString(Constants.NAME_CODE),
                                            Toast.LENGTH_SHORT).show();
                                    setContactsListViewAdapter();
                                    dialog.cancel();
                                    break;
                                case Constants.STATUS_ERROR:
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Connection Failed",
                                            Toast.LENGTH_SHORT).show();
                                    MainActivity.this.finish();
                                    break;
                                default:
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Incorrect credentials",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }else{
                            Log.e("MainActivity.java","NULL status_code");
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.registerLayout).setVisibility(View.GONE);
                view.findViewById(R.id.forgotPasswordLayout).setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
        //TODO: Implement Register New User

        TextView textRegister=view.findViewById(R.id.textRegister);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showRegister(view);
            }
        });

        //TODO: Implement Forgot Password
        TextView textForgotPassword=view.findViewById(R.id.textForgotPassword);
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPassword(view);
            }
        });
    }

    private void showRegister(final View view){
        view.findViewById(R.id.loginLayout).setVisibility(View.GONE);
        view.findViewById(R.id.registerLayout).setVisibility(View.VISIBLE);

        final EditText textRegisterName=view.findViewById(R.id.textRegisterName);
        final EditText textRegisterContactNo=view.findViewById(R.id.textRegisterContactNo);
        final Spinner spinnerRegisterSecurityQuestion=view.findViewById(R.id.spinnerRegisterSecurityQuestion);
        ArrayAdapter<CharSequence> arrayAdapterSecQuestion= ArrayAdapter.createFromResource(MainActivity.this,
                R.array.sec_questions,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerRegisterSecurityQuestion.setAdapter(arrayAdapterSecQuestion);
        spinnerRegisterSecurityQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sec_question =spinnerRegisterSecurityQuestion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText textRegisterSecurityAnswer=view.findViewById(R.id.textRegisterSecurityAnswer);
        final EditText textRegisterPassword=view.findViewById(R.id.textRegisterPassword);
        final EditText textRegisterConfirmPassword=view.findViewById(R.id.textRegisterConfirmPassword);
        Button register=view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textRegisterName.getText().toString().isEmpty()&&!textRegisterContactNo.getText().toString().isEmpty()&&!textRegisterSecurityAnswer.getText().toString().isEmpty()
                        &&!textRegisterPassword.getText().toString().isEmpty()&&!textRegisterConfirmPassword.getText().toString().isEmpty()){
                    if (textRegisterPassword.getText().toString().equals("" + textRegisterConfirmPassword.getText().toString())) {
                        DataExchange dataExchange = new DataExchange(MainActivity.this);
                        dataExchange.doRegister(textRegisterContactNo.getText().toString(),
                                textRegisterName.getText().toString(),
                                textRegisterPassword.getText().toString(),
                                sec_question,textRegisterSecurityAnswer.getText().toString(),
                                new OnTaskResult() {
                            @Override
                            public void onResult(Bundle bundle) {
                                String status_code = bundle.getString(Constants.STATUS_CODE);
                                if (status_code != null) {
                                    switch (status_code) {
                                        case Constants.STATUS_OK:
                                            String auth_code = (String) bundle.get(Constants.AUTH_CODE);
                                            PreferenceAdapter preferenceAdapter = new PreferenceAdapter(MainActivity.this, Constants.AUTH_PREFERENCE);

                                            preferenceAdapter.putStringPreference(Constants.AUTH_CODE, auth_code);
                                            preferenceAdapter.setLogin(true);
                                            setContentView(R.layout.activity_main);
                                            view.findViewById(R.id.registerLayout).setVisibility(View.GONE);
                                            view.findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);

                                            break;
                                        case Constants.STATUS_ERROR:
                                            Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                            break;
                                    }
                                } else {
                                    Log.e("MainActivity.java", "NULL status_code");
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "Passwords not matched", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showForgotPassword(final View view){
        view.findViewById(R.id.loginLayout).setVisibility(View.GONE);
        view.findViewById(R.id.forgotPasswordLayout).setVisibility(View.VISIBLE);

        //TODO: Complete Forgot Password

        final EditText textForgotContactNo=view.findViewById(R.id.textForgotContactNo);

        final Spinner spinnerForgotSecurityQuestion=view.findViewById(R.id.spinnerForgotSecurityQuestion);
        ArrayAdapter<CharSequence> arrayAdapterSecQuestion= ArrayAdapter.createFromResource(MainActivity.this,
                R.array.sec_questions,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerForgotSecurityQuestion.setAdapter(arrayAdapterSecQuestion);
        spinnerForgotSecurityQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sec_question =spinnerForgotSecurityQuestion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText textForgotSecurityAnswer=view.findViewById(R.id.textForgotSecurityAnswer);
        final EditText textForgotNewPassword=view.findViewById(R.id.textForgotNewPassword);
        final EditText textForgotConfirmPassword=view.findViewById(R.id.textForgotConfirmPassword);

        Button forgotPassword=view.findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textForgotNewPassword.getText().toString().equals("" + textForgotConfirmPassword.getText().toString())) {
                    DataExchange dataExchange = new DataExchange(MainActivity.this);
                    dataExchange.doForgotPassword(textForgotContactNo.getText().toString(), sec_question, textForgotSecurityAnswer.getText().toString(), textForgotNewPassword.getText().toString(), new OnTaskResult() {
                        @Override
                        public void onResult(Bundle bundle) {
                            String status_code = bundle.getString(Constants.STATUS_CODE);
                            if (status_code != null) {
                                switch (status_code) {
                                    case Constants.STATUS_OK:
                                        String auth_code = (String) bundle.get(Constants.AUTH_CODE);
                                        PreferenceAdapter preferenceAdapter = new PreferenceAdapter(MainActivity.this, Constants.AUTH_PREFERENCE);

                                        preferenceAdapter.putStringPreference(Constants.AUTH_CODE, auth_code);
                                        preferenceAdapter.setLogin(true);
                                        setContentView(R.layout.activity_main);
                                        view.findViewById(R.id.forgotPasswordLayout).setVisibility(View.GONE);
                                        view.findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);

                                        break;
                                    case Constants.STATUS_ERROR:
                                        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(MainActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();

                                        break;
                                }
                            } else {
                                Log.e("MainActivity.java", "NULL status_code");
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Passwords not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
