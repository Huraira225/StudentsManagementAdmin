package com.example.adminloginactivity.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminloginactivity.Dialogs.ApprovalItemsListActivity;
import com.example.adminloginactivity.MailAPI.JavaMailAPI;
import com.example.adminloginactivity.R;
import com.example.adminloginactivity.Classes.RegisterUsers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RejectedStudentsActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    DatabaseReference databaseReference;
    String approve;
    List<RegisterUsers> Users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_students);
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        listView=findViewById(R.id.listView_permissions);
        TextView textView=findViewById(R.id.textView_student_back);
        textView.setOnClickListener(this);
        Users = new ArrayList<>();
        databaseReference.orderByChild("approve").equalTo("reject").addValueEventListener(new ValueEventListener() {
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
                ApprovalItemsListActivity UserAdapter = new ApprovalItemsListActivity(RejectedStudentsActivity.this, Users);
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
                Call( User.getFirstname(),User.getLastname(),User.getUsername(),User.getCnic(),User.getPhoneno(),User.getEmail(),User.getApprove(),User.getId());
            }
        });
    }

    private void Call(final String firstname, final String lastname, final String username, final String cnic , final String phoneno, final String email, final String approve , final String id) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_approval_pop_up_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final TextView updateTextFirstname =  dialogView.findViewById(R.id.editText_firstname);
        final TextView updateTextLastname =  dialogView.findViewById(R.id.editText_lastname);
        final TextView updateTextUsername =  dialogView.findViewById(R.id.editText_username);
        final TextView updateTextCnic =  dialogView.findViewById(R.id.editText_cnic);
        final TextView updateTextPhoneno = dialogView.findViewById(R.id.editText_phoneNo);
        final TextView updateTextEmail =  dialogView.findViewById(R.id.editText_email);

        updateTextFirstname.setText(firstname);
        updateTextLastname.setText(lastname);
        updateTextUsername.setText(username);
        updateTextCnic.setText(cnic);
        updateTextPhoneno.setText(phoneno);
        updateTextEmail.setText(email);



        final Button buttonApprove = (Button) dialogView.findViewById(R.id.button_Approve);
        final Button buttonReject = (Button) dialogView.findViewById(R.id.button_Reject);
        //username for set dialog title
        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Click listener for Update data
        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String approve="yes";
                databaseReference.child(id).child("approve").setValue(approve);

                String subject="Student Management";
                String message="Registered your Account Please Login!";
                JavaMailAPI javaMailAPI=new JavaMailAPI(RejectedStudentsActivity.this, email,subject,message);
                javaMailAPI.execute();
                Toast.makeText(getApplicationContext(), "Student Approved", Toast.LENGTH_LONG).show();
                b.dismiss();
            }

        });
        // Click listener for Delete data
        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send Email With Java Mail Api
                b.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(RejectedStudentsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
