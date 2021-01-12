package bd.org.coronacare.profile.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

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

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Doctor;
import bd.org.coronacare.models.User;
import bd.org.coronacare.profile.view.doctor.DoctorAboutFragment;
import bd.org.coronacare.profile.view.doctor.DoctorFeedbackFragment;

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
                if (item.getItemId() == R.id.mndr_message) {
                    //sendMessage();
                } else if (item.getItemId() == R.id.mndr_appointment) {
                    //bookAppointment();
                } else if (item.getItemId() == R.id.mndr_feedback) {
                    //giveFeedback();
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

        loadDoctorData();
    }

    private void loadDoctorData() {
        mDatabase.child("users").child(doctorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    showAboutFragment();
                    Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(photo);
                    name.setText(mUser.getName());
                    Doctor mDoctor = mUser.getDoctor();
                    if (mDoctor!=null && mDoctor.getRating()!=null) {
                        rating.setRating(Float.parseFloat(mDoctor.getRating()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showAboutFragment();
                } else {
                    showFeedbacksFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showAboutFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.dp_frame, new DoctorAboutFragment(doctorID))
            .commit();
    }

    private void showFeedbacksFragment() {
        getSupportFragmentManager()
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.dp_frame, new DoctorFeedbackFragment(doctorID))
            .commit();
    }
}