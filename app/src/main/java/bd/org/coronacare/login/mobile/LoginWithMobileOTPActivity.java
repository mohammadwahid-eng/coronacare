package bd.org.coronacare.login.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import bd.org.coronacare.R;
import bd.org.coronacare.main.MainActivity;
import bd.org.coronacare.models.User;

public class LoginWithMobileOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private MaterialToolbar toolbar;
    private String number;
    private PinView verificationCode;
    private MaterialButton verifyBtn;
    private MaterialTextView resendBtn;

    private ProgressDialog preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_login_with_mobile_otp);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        toolbar             = findViewById(R.id.toolbar);
        number              = getIntent().getStringExtra("NUMBER");
        verificationCode    = findViewById(R.id.lwmo_code);
        verifyBtn           = findViewById(R.id.lwmo_verify_btn);
        resendBtn           = findViewById(R.id.lwmo_resend_btn);
        preLoader           = new ProgressDialog(this, R.style.AppTheme_ProgressDialog);

        preLoader.setCanceledOnTouchOutside(false);

        verificationCode.requestFocus();
        verifyBtn.setOnClickListener(this);
        resendBtn.setOnClickListener(this);
        toolbar.setTitle("OTP Verification");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginWithMobileOTPActivity.this, LoginWithMobileActivity.class));
                finish();
            }
        });
        sendOTP();
    }

    private void sendOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        Toast.makeText(LoginWithMobileOTPActivity.this, "Verification code resent.", Toast.LENGTH_LONG).show();
        showCountDownTimer();
    }

    private void showCountDownTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resendBtn.setTextColor(getResources().getColor(android.R.color.darker_gray));
                resendBtn.setEnabled(false);
                resendBtn.setText("Wait " + millisUntilFinished/1000 + "s");
            }
            public void onFinish() {
                resendBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                resendBtn.setEnabled(true);
                resendBtn.setText(getString(R.string.resend_code));
            }
        }.start();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken    = forceResendingToken;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(LoginWithMobileOTPActivity.this, "Invalid request", Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(LoginWithMobileOTPActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginWithMobileOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        preLoader.setMessage("Processing verification...");
        preLoader.show();

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = Objects.requireNonNull(task.getResult()).getUser();
                    updateUI(currentUser);
                } else {
                    preLoader.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(LoginWithMobileOTPActivity.this, "Invalid verification code", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginWithMobileOTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void updateUI(final FirebaseUser currentUser) {
        mDatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    User cUser = new User();
                    cUser.setId(currentUser.getUid());
                    cUser.setName("Your name");
                    cUser.setMobile(currentUser.getPhoneNumber());
                    cUser.setPhoto("https://ui-avatars.com/api/?font-size=0.33&background=252427&color=fff&bold=true&size=200&name=Your+Name");
                    mDatabase.child("users").child(currentUser.getUid()).setValue(cUser);
                }

                startActivity(new Intent(LoginWithMobileOTPActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.equals(verifyBtn)) {
            String code = Objects.requireNonNull(verificationCode.getText()).toString();
            if (code.isEmpty()) {
                Toast.makeText(LoginWithMobileOTPActivity.this, "Verification code is required", Toast.LENGTH_SHORT).show();
            } else if(code.length() != 6) {
                Toast.makeText(LoginWithMobileOTPActivity.this, "Verification code must be 6 digits", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }

        } else if(v.equals(resendBtn)) {
            resendOTP();
        }
    }
}