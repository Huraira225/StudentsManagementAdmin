package com.example.adminloginactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.adminloginactivity.Dialogs.ItemsListActivity;
import com.example.adminloginactivity.R;
import com.example.adminloginactivity.Classes.RegisterUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    DatabaseReference databaseReference;

    List<RegisterUsers> Users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        databaseReference= FirebaseDatabase.getInstance("https://databaseregisterationuser-default-rtdb.firebaseio.com/").getReference("Students");
        listView=findViewById(R.id.listViewUsers);


        TextView textView=findViewById(R.id.textView_student_back);
        textView.setOnClickListener(this);
        Users = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous User list
                Users.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting User from firebase console
                    RegisterUsers User = postSnapshot.getValue(RegisterUsers.class);
                    //adding User to the list
                    Users.add(User);
                }
                //creating Userlist adapter
                ItemsListActivity UserAdapter = new ItemsListActivity(StudentActivity.this, Users);
                //attaching adapter to the listview
                listView.setAdapter(UserAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RegisterUsers User = Users.get(i);
                CallUpdateAndDeleteDialog( User.getFirstname(),User.getLastname(),User.getUsername(),User.getCnic(),User.getPhoneno(),User.getEmail(),User.getApprove(),User.getId());
            }
        });
    }

    private void CallUpdateAndDeleteDialog( String firstname,String lastname, String username,String cnic , String phoneno, final String email,String approve,final String id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_students_pop_up_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextFirstname = (EditText) dialogView.findViewById(R.id.editText_firstname);
        final EditText updateTextLastname = (EditText) dialogView.findViewById(R.id.editText_lastname);
        final EditText updateTextUsername = (EditText) dialogView.findViewById(R.id.editText_username);
        final EditText updateTextCnic = (EditText) dialogView.findViewById(R.id.editText_cnic);
        final EditText updateTextPhoneno = (EditText) dialogView.findViewById(R.id.editText_phoneNo);
        final EditText updateTextEmail = (EditText) dialogView.findViewById(R.id.editText_email);

        updateTextFirstname.setText(firstname);
        updateTextLastname.setText(lastname);
        updateTextUsername.setText(username);
        updateTextCnic.setText(cnic);
        updateTextPhoneno.setText(phoneno);
        updateTextEmail.setText(email);


        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.button_Update);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.button_Delete);
        //username for set dialog title
        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = updateTextFirstname.getText().toString().trim();
                String lastname = updateTextLastname.getText().toString().trim();
                String username = updateTextUsername.getText().toString().trim();
                String cnic = updateTextCnic.getText().toString().trim();
                String phoneno = updateTextPhoneno.getText().toString().trim();
                String email = updateTextEmail.getText().toString().trim();
                String approve="No";
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(firstname)) {
                    if (!TextUtils.isEmpty(lastname)) {
                        if (!TextUtils.isEmpty(username)) {
                            if (!TextUtils.isEmpty(cnic)) {
                                if (!TextUtils.isEmpty(phoneno)) {
                                    if (!TextUtils.isEmpty(email)) {
                                        //Method for update data
                                        updateUser(firstname,lastname,username,cnic,phoneno,email,approve,id);
                                        b.dismiss();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

       // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(id);
                b.dismiss();
            }
        });
    }

    private boolean updateUser( String firstname,String lastname,String username,String cnic,String phoneno, String email,String approval,String id) {
        //getting the specified User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("Students").child(id);
        RegisterUsers User = new RegisterUsers(firstname,lastname,username,cnic,phoneno, email,approval,id);
        //update  User  to firebase
        UpdateReference.setValue(User);
        Toast.makeText(getApplicationContext(), "User Updated", Toast.LENGTH_LONG).show();
        return true;
    }
    private boolean deleteUser(String id) {
        //getting the specified User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("Students").child(id);
        //removing User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(StudentActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
