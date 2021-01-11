package bd.org.coronacare.login.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import bd.org.coronacare.R;
import bd.org.coronacare.login.LoginOptionsActivity;

public class LoginWithMobileActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialToolbar toolbar;
    private TextInputEditText number;
    private MaterialButton contBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_mobile);

        toolbar = findViewById(R.id.toolbar);
        number  = findViewById(R.id.lwm_number);
        contBtn = findViewById(R.id.lwm_cont_btn);

        contBtn.setOnClickListener(this);
        toolbar.setTitle("Mobile Verification");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithMobileActivity.this, LoginOptionsActivity.class));
                finish();
            }
        });
    }

    private void processNumber(Editable field) {
        String number = field.toString();
        if (field.length() != 11) {
            Toast.makeText(LoginWithMobileActivity.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        } else {
            if (!number.contains("+88")) {
                number = "+88" + number;
            } else if (!number.contains("+880")) {
                number = "+880" + number;
            }
            startActivity(new Intent(LoginWithMobileActivity.this, LoginWithMobileOTPActivity.class).putExtra("NUMBER", number));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(contBtn)) {
            processNumber(Objects.requireNonNull(number.getText()));
        }
    }
}