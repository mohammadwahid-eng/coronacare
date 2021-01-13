package bd.org.coronacare.doctors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
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
import bd.org.coronacare.profile.view.DoctorProfileActivity;
import bd.org.coronacare.utils.DividerItemDecorator;

public class DoctorsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private MaterialToolbar toolbar;
    private RecyclerView doctorList;
    private List<User> doctors = new ArrayList<>();
    private DoctorAdapter adapter;
    private FloatingActionButton fab;
    private ShimmerFrameLayout shimmerDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        toolbar = findViewById(R.id.toolbar);
        doctorList = findViewById(R.id.doc_list);
        fab = findViewById(R.id.doc_fab);
        fab.setOnClickListener(this);

        shimmerDoctors = findViewById(R.id.shmrdoc_list);

        toolbar.setTitle("Nearby Doctors of You");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showDoctors();
    }

    private void showDoctors() {
        adapter = new DoctorAdapter(this, doctors);
        doctorList.setLayoutManager(new LinearLayoutManager(this));
        doctorList.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.gr_line_horizontal)));
        doctorList.setHasFixedSize(true);
        doctorList.setAdapter(adapter);

        mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    loadDoctorList(null, mUser.getThana(), mUser.getDistrict());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDoctorList(String specialization, String thana, String district) {
        doctors.clear();
        adapter.notifyDataSetChanged();

        mDatabase.child("users").orderByChild("doctor/hthana").equalTo(thana).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataThanaSnapshot) {
                shimmerDoctors.stopShimmer();
                shimmerDoctors.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataThanaSnapshot.getChildren()) {
                    User mUser = snapshot.getValue(User.class);
                    if (mUser!=null && !mUser.getId().equals(mAuth.getUid())) {
                        Doctor mDoctor = mUser.getDoctor();
                        if (mDoctor!=null && mDoctor.isService()) {
                            if (specialization == null) {
                                doctors.add(mUser);
                                adapter.notifyDataSetChanged();
                            } else {
                                if (mDoctor.getSpecializations().contains(specialization)) {
                                    doctors.add(mUser);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(fab)) {
            startActivityForResult(new Intent(DoctorsActivity.this, DoctorSearchActivity.class), 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data!=null) {
            loadDoctorList(data.getStringExtra("SPECIALIZATION"), data.getStringExtra("THANA"), data.getStringExtra("DISTRICT"));
        }
    }

    public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
        private Context mContext;
        private List<User> doctors;

        public DoctorAdapter(Context mContext, List<User> doctors) {
            this.mContext = mContext;
            this.doctors = doctors;
        }

        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DoctorAdapter.DoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nearby_doctor_y, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            Picasso.get().load(doctors.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
            holder.name.setText(doctors.get(position).getName());
            holder.specializations.setText(doctors.get(position).getDoctor().getSpecializations());
            holder.hospital.setText(doctors.get(position).getDoctor().getHname());
            holder.doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, DoctorProfileActivity.class).putExtra("USERID", doctors.get(position).getId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return doctors.size();
        }

        public class DoctorViewHolder extends RecyclerView.ViewHolder{
            private RelativeLayout doctor;
            private CircularImageView photo;
            private MaterialTextView name;
            private MaterialTextView specializations;
            private MaterialTextView hospital;

            public DoctorViewHolder(@NonNull View itemView) {
                super(itemView);
                doctor = itemView.findViewById(R.id.doc);
                photo = itemView.findViewById(R.id.doc_photo);
                name = itemView.findViewById(R.id.doc_name);
                specializations = itemView.findViewById(R.id.doc_specializations);
                hospital = itemView.findViewById(R.id.doc_hospital);
            }
        }
    }
}