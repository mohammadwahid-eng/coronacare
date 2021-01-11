package bd.org.coronacare.profile.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
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

public class DoctorProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String doctorID;
    private MaterialToolbar toolbar;
    private CircularImageView photo;
    private MaterialTextView name;
    private RatingBar rating;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        doctorID = getIntent().getStringExtra("USERID");
        toolbar = findViewById(R.id.toolbar);
        photo = findViewById(R.id.dp_photo);
        name = findViewById(R.id.dp_name);
        rating = findViewById(R.id.dp_rating);
        tabLayout = findViewById(R.id.dp_tab_layout);

        toolbar.setTitle("Doctor's Profile");
        toolbar.inflateMenu(R.menu.menu_doctor_profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.ab_dr_message) {
//                    sendMessage();
//                } else if (item.getItemId() == R.id.ab_dr_appointment) {
//                    bookAppointment();
//                } else if (item.getItemId() == R.id.ab_dr_feedback) {
//                    giveFeedback();
//                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        loadDoctorData();
    }

    private void loadDoctorData() {
        mDatabase.child("users").child(doctorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(photo);
                    name.setText(mUser.getName());
//                    if (mUser.getDoctor()!=null) {
//                        drMobileNo = mUser.getDoctor().getHmobile();
//                        float mRating= 0f;
//                        if (mUser.getDoctor().getRating()!=null) {
//                            mRating = Float.parseFloat(mUser.getDoctor().getRating());
//                        }
//                        rating.setRating(mRating);
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}