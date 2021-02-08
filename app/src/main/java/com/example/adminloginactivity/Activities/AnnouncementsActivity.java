package com.example.adminloginactivity.Activities;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminloginactivity.Classes.AnnouncementsGetterSetter;
import com.example.adminloginactivity.Adapters.AnnouncementsList;
import com.example.adminloginactivity.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity implements View.OnClickListener {


    //initialize
    EditText editTextTile, editTextAnnouncements;
    Button buttonAnnouncements;
    ListView listViewUsers;


    //a list to store all the User from firebase database
    List<AnnouncementsGetterSetter> Users;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        TextView textView= findViewById(R.id.textView_announcement_back);
        databaseReference = FirebaseDatabase.getInstance("https://databaseregisterationuser-default-rtdb.firebaseio.com/").getReference("Announcements");

        editTextTile = (EditText) findViewById(R.id.editText_title);
        editTextAnnouncements = (EditText) findViewById(R.id.editText_announcements);
        listViewUsers = (ListView) findViewById(R.id.listView_announcements);
        buttonAnnouncements = (Button) findViewById(R.id.button_announcement);
        //list for store objects of user
        Users = new ArrayList<>();
        // to maintan click listener of views
        textView.setOnClickListener(this);
        buttonAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUser()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });

        // list item click listener
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AnnouncementsGetterSetter User = Users.get(i);
                CallUpdateAndDeleteDialog(User.getUserid(), User.getTile(),User.getAnncuncements());
            }
        });

           }





    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous User list
                Users.clear();

             //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    AnnouncementsGetterSetter User = postSnapshot.getValue(AnnouncementsGetterSetter.class);
                    //adding User to the list
                    Users.add(User);
                }
                //creating Userlist adapter
                AnnouncementsList UserAdapter = new AnnouncementsList(AnnouncementsActivity.this, Users);
                //attaching adapter to the listview
                listViewUsers.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CallUpdateAndDeleteDialog(final String userid, final String title, final String announcements) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.activity_announcements_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateText_title);
        final EditText updateTextemail = (EditText) dialogView.findViewById(R.id.updateText_anncuncements);
        updateTextname.setText(title);
        updateTextemail.setText(announcements);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.button_Update);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.button_Delete);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = updateTextname.getText().toString().trim();
                String announcements = updateTextemail.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(title)) {
                    if (!TextUtils.isEmpty(announcements)) {

                            //Method for update data
                            updateUser(userid, title, announcements);
                            b.dismiss();
                    }
                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(userid);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String title, String announcements) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Announcements").child(id);
        AnnouncementsGetterSetter User = new AnnouncementsGetterSetter(id, title, announcements);
        //update  User  to firebase
        UpdateReference.setValue(User);
        Toast.makeText(getApplicationContext(), "Announcements Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Announcements").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Announcement Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


    private void addUser() {


        //getting the values to save
        String name = editTextTile.getText().toString().trim();
        String email = editTextAnnouncements.getText().toString().trim();


        //checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(email)) {


                    //it will create a unique id and we will use it as the Primary Key for our User
                    String id = databaseReference.push().getKey();
                    //creating an User Object
                    AnnouncementsGetterSetter User = new AnnouncementsGetterSetter(id, name, email);
                    //Saving the User
                    databaseReference.child(id).setValue(User);

                    editTextTile.setText("");
                    editTextAnnouncements.setText("");
                    Toast.makeText(this, "Announcements added", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please enter a Announcements", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter a Tile", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
            Intent intent = new Intent(AnnouncementsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
    }
}