package bd.org.coronacare.profile.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import bd.org.coronacare.R;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DateTimeFormat;

public class UserProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private MaterialToolbar toolbar;
    private CircularImageView photo;
    private MaterialTextView name;
    private MaterialTextView age;
    private MaterialTextView gender;
    private MaterialTextView bgroup;
    private MaterialTextView mobile;
    private MaterialTextView thana;
    private MaterialTextView district;
    private MaterialTextView occupation;
    private MaterialTextView lastActive;
    private String userID;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        toolbar = findViewById(R.id.toolbar);
        photo = findViewById(R.id.up_photo);
        name = findViewById(R.id.up_name);
        age = findViewById(R.id.up_age);
        gender = findViewById(R.id.up_gender);
        bgroup = findViewById(R.id.up_bgroup);
        mobile = findViewById(R.id.up_mobile);
        thana = findViewById(R.id.up_thana);
        district = findViewById(R.id.up_district);
        occupation = findViewById(R.id.up_occupation);
        lastActive = findViewById(R.id.up_last_active);
        userID = getIntent().getStringExtra("USERID");

        toolbar.setTitle("Donor's Profile");
        toolbar.inflateMenu(R.menu.menu_user_profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuu_call) {
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo)));
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadDonorData();
    }

    private void loadDonorData() {
        mDatabase.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(photo);
                    name.setText(mUser.getName());
                    age.setText(DateTimeFormat.calculateAge(mUser.getDob()));
                    if (!TextUtils.isEmpty(age.getText())) {
                        age.append(" Years");
                    }
                    gender.setText(mUser.getGender());
                    bgroup.setText(mUser.getBgroup());
                    mobile.setText(mUser.getMobile());
                    thana.setText(mUser.getThana());
                    district.setText(mUser.getDistrict());
                    occupation.setText(mUser.getOccupation());
                    lastActive.setText(DateTimeFormat.getLastActive(mUser.getLastOnline()));
                    mobileNo = mUser.getMobile();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}