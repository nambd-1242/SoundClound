package com.framgia.mysoundcloud.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.framgia.mysoundcloud.R;
import com.framgia.mysoundcloud.data.model.User;
import com.framgia.mysoundcloud.screen.main.MainActivity;
import com.framgia.mysoundcloud.utils.Constant;
import com.framgia.mysoundcloud.utils.Navigator;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContact.LoginView {
    private static final String STATE = LoginActivity.class.getCanonicalName();
    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View view) {
        presenter.login();
    }

    @Override
    public void onSuccess(User user) {
        if (user != null) {
            openMainActivity(user);
        }

    }

    private void openMainActivity(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.USER , user);
        new Navigator(this).startActivity(MainActivity.class, false);
        finish();
    }

    @Override
    public void showDialogLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, "Login Failure ! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(task);
        }
    }

}
