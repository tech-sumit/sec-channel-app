package tech.profusion.sumit.securecommunicator.framework.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import tech.profusion.sumit.securecommunicator.framework.entities.Constants;
import tech.profusion.sumit.securecommunicator.framework.entities.Contact;

/**
 * Created by Sumit Agrawal on 12-01-2018.
 */

public class DBHandler extends SQLiteOpenHelper{

    public DBHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_CONTACT_NO, contact.getContactNo());
        values.put(Constants.COLUMN_NAME, contact.getName());
        values.put(Constants.COLUMN_PASSWORD, contact.getPassword());

        long insert_id = db.insert(Constants.TABLE_CONTACTS, null, values);

        Log.i("Contact Inserted","Insert ID: "+insert_id);
    }

    public Contact getContact(String contact_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_CONTACTS+ " WHERE "
                + Constants.COLUMN_CONTACT_NO+ " = '" + contact_id+"'";
        Log.e("Select Query", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        Contact contact = new Contact();
        contact.setContactNo(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTACT_NO)));
        contact.setName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME)));
        contact.setPassword(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PASSWORD)));
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_CONTACTS;

        Log.e("Select All Query", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while (cursor.moveToNext()){
            Contact contact = new Contact();
            contact.setName(cursor.getString((cursor.getColumnIndex(Constants.COLUMN_NAME))));
            contact.setContactNo(cursor.getString((cursor.getColumnIndex(Constants.COLUMN_CONTACT_NO))));
            contact.setPassword(cursor.getString((cursor.getColumnIndex(Constants.COLUMN_PASSWORD))));
            contacts.add(contact);
        }
        return contacts;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, contact.getName());
        values.put(Constants.COLUMN_CONTACT_NO, contact.getContactNo());
        values.put(Constants.COLUMN_PASSWORD, contact.getPassword());
        return db.update(Constants.TABLE_CONTACTS, values, Constants.COLUMN_CONTACT_NO+ " = ?",
                new String[] { contact.getContactNo() });
    }

    public void deleteContact(String contact_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_CONTACTS, Constants.COLUMN_CONTACT_NO+ " = ?",
                new String[] { contact_no });
    }
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+Constants.TABLE_CONTACTS);
    }
}
