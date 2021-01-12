package bd.org.coronacare.profile.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.org.coronacare.R;
import bd.org.coronacare.main.chat.conversation.ConversationActivity;
import bd.org.coronacare.models.Doctor;
import bd.org.coronacare.models.Feedback;
import bd.org.coronacare.models.User;
import bd.org.coronacare.profile.view.doctor.DoctorAboutFragment;
import bd.org.coronacare.profile.view.doctor.DoctorFeedbackFragment;

public class DoctorProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String doctorID;
    private String doctorMobileNo;
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
                    sendMessage();
                } else if (item.getItemId() == R.id.mndr_appointment) {
                    callForAppointment();
                } else if (item.getItemId() == R.id.mndr_feedback) {
                    giveFeedback();
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
                    if (mDoctor!=null) {
                        doctorMobileNo = mDoctor.getHmobile();
                        if (mDoctor.getRating()!=null) {
                            rating.setRating(Float.parseFloat(mDoctor.getRating()));
                        }
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

    private void sendMessage() {
        startActivity(new Intent(DoctorProfileActivity.this, ConversationActivity.class).putExtra("USERID", doctorID));
    }

    private void callForAppointment() {
        if (TextUtils.isEmpty(doctorMobileNo)) {
            Toast.makeText(DoctorProfileActivity.this, "Hospital's mobile no not found", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + doctorMobileNo)));
        }
    }

    private void giveFeedback() {
        FeedbackBottomSheet dialog = new FeedbackBottomSheet(doctorID);
        dialog.show(getSupportFragmentManager(), "FEEDBACK");
    }

    public static class FeedbackBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;
        private String doctorID;

        private TextInputEditText comment;
        private RatingBar score;
        private MaterialButton btn;
        private ProgressDialog preLoader;

        public FeedbackBottomSheet(String doctorID) {
            this.doctorID = doctorID;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            preLoader = new ProgressDialog(getActivity(), R.style.AppTheme_ProgressDialog);
            preLoader.setMessage("Submitting...");
            preLoader.setCanceledOnTouchOutside(false);

            View dialog = inflater.inflate(R.layout.layout_bs_feedback, container, false);
            comment = dialog.findViewById(R.id.fdbk_comment);
            score = dialog.findViewById(R.id.fdbk_score);
            btn = dialog.findViewById(R.id.fdbk_btn);
            btn.setOnClickListener(this);
            return dialog;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(btn)) {
                if (!TextUtils.isEmpty(comment.getText())) {
                    preLoader.show();
                    Map feedback = new HashMap<>();
                    feedback.put("id", mAuth.getUid());
                    feedback.put("comment", comment.getText().toString());
                    feedback.put("score", score.getRating());
                    feedback.put("time", ServerValue.TIMESTAMP);

                    mDatabase.child("users").child(doctorID).child("doctor").child("feedbacks").child(mAuth.getUid()).setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ratingCalculation();
                        }
                    });
                }
            }
        }

        private void ratingCalculation() {
            mDatabase.child("users").child(doctorID).child("doctor").child("feedbacks").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataFeedbackSnapshot) {
                    float sum = 0;
                    int items = 0;
                    for (DataSnapshot snapshot : dataFeedbackSnapshot.getChildren()) {
                        Feedback mfeedback = snapshot.getValue(Feedback.class);
                        sum += mfeedback.getScore();
                        items++;
                    }
                    mDatabase.child("users").child(doctorID).child("doctor").child("rating").setValue(String.valueOf(sum/items));
                    Toast.makeText(getActivity(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                    preLoader.dismiss();
                    dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}