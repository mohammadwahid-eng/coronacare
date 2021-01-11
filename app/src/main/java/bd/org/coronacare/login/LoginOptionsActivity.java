package bd.org.coronacare.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import bd.org.coronacare.R;
import bd.org.coronacare.login.mobile.LoginWithMobileActivity;

public class LoginOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton mobileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);

        mobileBtn = findViewById(R.id.lgm_btn);
        mobileBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mobileBtn)) {
            startActivity(new Intent(LoginOptionsActivity.this, LoginWithMobileActivity.class));
            finish();
        }
    }
}