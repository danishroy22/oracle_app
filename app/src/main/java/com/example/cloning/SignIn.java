package com.example.cloning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cloning.Model.Users;
import com.example.cloning.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
ActivitySignInBinding Binding2;
ProgressDialog progressDialog;
FirebaseAuth mAuth;
FirebaseDatabase firebaseDatabase;
GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding2 = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(Binding2.getRoot());
        
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        
        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait\n Validation in Progress");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Binding2.btnSignIn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View View) {
               if (!Binding2.txtEmail.getText().toString().isEmpty() && !Binding2.txtPassword.getText().toString().isEmpty()) {

                   progressDialog.show();

                   mAuth.signInWithEmailAndPassword(Binding2.txtEmail.getText().toString(), Binding2.txtPassword.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   progressDialog.dismiss();
                                   if (task.isSuccessful()) {

                                       Intent Intent = new Intent(SignIn.this, MainActivity.class);
                                       startActivity(Intent);
                                   } else {

                                       Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                   }

                               }


                           });
               }
               else{
                   Toast.makeText(SignIn.this, "Enter Credential", Toast.LENGTH_SHORT).show();
               }

           }


        });
if(mAuth.getCurrentUser()!=null){
   Intent intent = new Intent(SignIn.this, MainActivity.class);
    startActivity(intent);

}

Binding2.txtClickHeretoSignUp.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View View) {
Intent intent2 = new Intent(SignIn.this, SignUp.class);
startActivity(intent2);
}
    });
Binding2.btnGoogle.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        signIn();
    }
});



    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
// Result returned from launching the Intent from GoogleSignInApi.getSign In Intent(...); if (requestCode == RC_SIGN_IN) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
// Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
// Google Sign In failed, update UI appropriately
            Log.w("TAG", "Google sign in failed", e);
        }
    }

        private void firebaseAuthWithGoogle(String idToken){
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Users users = new Users();
                                users.setUserId(user.getUid());
                                users.setUserName(user.getDisplayName());
                                users.setProfilePic(user.getPhotoUrl().toString());
                                firebaseDatabase.getReference().child("Users").child(users.getUserId()).setValue(users);

                                Intent intent3 = new Intent(SignIn.this, MainActivity.class);
                                startActivity(intent3);

                                Toast.makeText(SignIn.this, "Signed In with Google", Toast.LENGTH_SHORT).show();

                            }
                            else {

                            }
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential: failure", task.getException());
                        }

                        });
            }
        }
