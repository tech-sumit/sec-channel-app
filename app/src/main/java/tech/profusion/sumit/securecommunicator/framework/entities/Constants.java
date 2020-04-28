package tech.profusion.sumit.securecommunicator.framework.entities;

/**
 * Created by Sumit Agrawal on 12-01-2018.
 */

public final class Constants {
    //Strings
    public static final String LOGIN_STATUS="login_status";
    public static final String AUTH_PREFERENCE="auth_pref";

    //DB Constants
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "contactsManager";

    // Table Names
    public static final String TABLE_CONTACTS= "contacts";

    // TABLE_CONTACTS column names
    public static final String COLUMN_CONTACT_NO="column_contact_no";
    public static final String COLUMN_NAME="column_name";
    public static final String COLUMN_PASSWORD="column_password";

    //CREATE TABLE TABLE_CONTACTS
    public static final String CREATE_TABLE_CONTACTS="CREATE TABLE "+TABLE_CONTACTS+"("+
            COLUMN_CONTACT_NO+ " VARCHAR PRIMARY KEY," +
            COLUMN_NAME+ " TEXT," +
            COLUMN_PASSWORD + " TEXT" +
            ");";
/*
    //URLs
    public static final String LOGIN_URL="http://profusion.tech/secure_channel/v1/login";
    public static final String REGISTER_URL="http://profusion.tech/secure_channel/v1/register";
    public static final String RECOVER_URL="http://profusion.tech/secure_channel/v1/recover";
    public static final String SEND_URL="http://profusion.tech/secure_channel/v1/send";
    public static final String RECEIVE_URL ="http://profusion.tech/secure_channel/v1/receive";
    public static final String VALIDATE_URL ="http://profusion.tech/secure_channel/v1/validate";
*/
    //URLs
    public static final String BASE_URL="http://ec2-52-23-180-191.compute-1.amazonaws.com/secure_channel/v1";
    public static final String LOGIN_URL=BASE_URL+"/login";
    public static final String REGISTER_URL=BASE_URL+"/register";
    public static final String RECOVER_URL=BASE_URL+"/recover";
    public static final String SEND_URL=BASE_URL+"/send";
    public static final String RECEIVE_URL =BASE_URL+"/receive";
    public static final String VALIDATE_URL =BASE_URL+"/validate";

    //POST Params
    public static final String CONTACT_NO="contact_no";
    public static final String NAME="name";
    public static final String SEC_QUESTION="sec_question";
    public static final String SEC_ANSWER="sec_answer";
    public static final String PASSWORD="password";
    public static final String TARGET_CONTACT_NO="target";
    public static final String MESSAGE="message";
    public static final String TIMESTAMP ="timestamp";

    //REPLAY Prams
    public static final String AUTH_CODE="authorization";
    public static final String STATUS_CODE="status";
    public static final String NAME_CODE="name";

    //Status Codes
    public static final String STATUS_OK="701";
    public static final String STATUS_FALSE="702";
    public static final String STATUS_ERROR="703";

}
