package bd.org.coronacare.profile.edit.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import bd.org.coronacare.R;

public class ChangeMobileOTPActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_change_mobile_otp);

        mAuth               = FirebaseAuth.getInstance();
        mDatabase           = FirebaseDatabase.getInstance().getReference();
        toolbar             = findViewById(R.id.toolbar);
        number              = getIntent().getStringExtra("NUMBER");
        verificationCode    = findViewById(R.id.cm_code);
        verifyBtn           = findViewById(R.id.cm_verify_btn);
        resendBtn           = findViewById(R.id.cm_resend_btn);
        preLoader           = new ProgressDialog(this, R.style.AppTheme_ProgressDialog);
        preLoader.setCanceledOnTouchOutside(false);

        verificationCode.requestFocus();
        verifyBtn.setOnClickListener(this);
        resendBtn.setOnClickListener(this);
        toolbar.setTitle("OTP Verification");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        Toast.makeText(ChangeMobileOTPActivity.this, "OTP has sent", Toast.LENGTH_LONG).show();
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
            updateMobileNumber(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(ChangeMobileOTPActivity.this, "Invalid request", Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(ChangeMobileOTPActivity.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ChangeMobileOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void updateMobileNumber(PhoneAuthCredential phoneAuthCredential) {
        preLoader.setMessage("Processing verification...");
        preLoader.show();


        mAuth.getCurrentUser().updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    preLoader.dismiss();
                    mDatabase.child("users").child(mAuth.getUid()).child("mobile").setValue(number);
                    Toast.makeText(ChangeMobileOTPActivity.this, "Mobile number has updated", Toast.LENGTH_SHORT).show();
                    setResult(100, new Intent());
                    finish();
                } else {
                    preLoader.dismiss();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(ChangeMobileOTPActivity.this, "Invalid verification code", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ChangeMobileOTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(verifyBtn)) {
            String code = Objects.requireNonNull(verificationCode.getText()).toString();
            if (code.isEmpty()) {
                Toast.makeText(ChangeMobileOTPActivity.this, "Verification code is required", Toast.LENGTH_SHORT).show();
            } else if(code.length() != 6) {
                Toast.makeText(ChangeMobileOTPActivity.this, "Verification code must be 6 digits", Toast.LENGTH_SHORT).show();
            } else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                updateMobileNumber(credential);
            }

        } else if(v.equals(resendBtn)) {
            resendOTP();
        }
    }
}