package tech.profusion.sumit.securecommunicator.framework.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tech.profusion.sumit.securecommunicator.R;
import tech.profusion.sumit.securecommunicator.framework.entities.Contact;

/**
 * Created by Sumit Agrawal on 30-01-2018.
 */

public class ContactListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater=null;
    private Contact contact;
    private ArrayList data;
    public ContactListAdapter(Activity activity, ArrayList data){
        this.data = data;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return contact;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.chat_list_item_layout, null);

            holder = new ViewHolder();
            holder.contactName= (TextView) view.findViewById(R.id.name_view);
            holder.contactNo= (TextView) view.findViewById(R.id.number_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() <= 0) {
            holder.contactName.setText("Click add button to start communication");
        }
        else {
            contact = null;
            contact= (Contact) data.get(position);

            holder.contactName.setText(contact.getName());
            holder.contactNo.setText(contact.getContactNo());
        }
        return view;
    }
    public static class ViewHolder {
        TextView contactNo;
        TextView contactName;
    }
}
