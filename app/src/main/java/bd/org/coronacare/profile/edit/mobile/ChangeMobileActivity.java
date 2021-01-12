package bd.org.coronacare.profile.edit.mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import bd.org.coronacare.R;

public class ChangeMobileActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private MaterialToolbar toolbar;
    private TextInputEditText number;
    private MaterialButton contBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        toolbar = findViewById(R.id.toolbar);
        number  = findViewById(R.id.cm_number);
        contBtn = findViewById(R.id.cm_cont_btn);


        contBtn.setOnClickListener(this);
        toolbar.setTitle("Change Mobile Number");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void processNumber(Editable field) {
        String number = field.toString();
        if (field.length() != 11) {
            Toast.makeText(ChangeMobileActivity.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
        } else {
            if (!number.contains("+88")) {
                number = "+88" + number;
            } else if (!number.contains("+880")) {
                number = "+880" + number;
            }

            String finalNumber = number;
            mDatabase.child("users").orderByChild("mobile").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(ChangeMobileActivity.this, "Mobile number already registered", Toast.LENGTH_LONG).show();
                    } else {
                        startActivityForResult(new Intent(ChangeMobileActivity.this, ChangeMobileOTPActivity.class).putExtra("NUMBER", finalNumber), 100);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(contBtn)) {
            processNumber(Objects.requireNonNull(number.getText()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            finish();
        }
    }
}