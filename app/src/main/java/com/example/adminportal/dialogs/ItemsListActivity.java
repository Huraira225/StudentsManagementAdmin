package com.example.adminportal.dialogs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adminportal.classes.RegisterUsers;
import com.example.adminportal.R;

import java.util.List;

public class ItemsListActivity extends ArrayAdapter<RegisterUsers> {

    private Activity context;
    //list of users
    List<RegisterUsers> Users;
    public ItemsListActivity(Activity context, List<RegisterUsers> Users) {
        super(context, R.layout.layout_classfellows_list_items, Users);
        this.context = context;
        this.Users = Users;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_classfellows_list_items, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textview_Header_name);
        TextView textviewemail = (TextView) listViewItem.findViewById(R.id.textview_Header_email);
        //getting user at position
        RegisterUsers User = Users.get(position);
        //set user name
        textViewName.setText(User.getUsername());
        //set user email
        textviewemail.setText(User.getEmail ());
        //set user mobilenumber
        return listViewItem;
    }
}
