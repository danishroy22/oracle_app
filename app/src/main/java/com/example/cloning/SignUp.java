package com.example.cloning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cloning.Model.Users;
import com.example.cloning.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ActivitySignUpBinding Binding;
    FirebaseDatabase Database;
    ProgressDialog ProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance();
        ProgressDialog = new ProgressDialog(SignUp.this);
        ProgressDialog.setTitle("Creating Account");
        ProgressDialog.setMessage("We are creating your account");

        Binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Binding.txtUsername.getText().toString().isEmpty() && !Binding.txtEmail.getText().toString().isEmpty() && !Binding.txtPassword.getText().toString().isEmpty()){

                    mAuth.createUserWithEmailAndPassword(Binding.txtEmail.getText().toString(), Binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    ProgressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                                        Users user = new Users(Binding.txtUsername.getText().toString(), Binding.txtEmail.getText().toString(), Binding.txtPassword.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        Database.getReference().child("Users").child(id).setValue(user);

                                    }
                                    else {
                                        Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(SignUp.this, "Enter Credential", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View View) {
                Intent intent2 = new Intent(SignUp.this, SignIn.class);
                startActivity(intent2);
            }
        });


    }
}