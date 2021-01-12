package bd.org.coronacare.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import bd.org.coronacare.doctors.DoctorsActivity;
import bd.org.coronacare.models.Doctor;
import bd.org.coronacare.models.Tips;
import bd.org.coronacare.models.User;
import bd.org.coronacare.profile.view.DoctorProfileActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private MaterialTextView currentUserName;
    private CircularImageView currentUserPhoto;

    private MaterialTextView seeAllBtn;
    private RecyclerView tipsList;
    private RecyclerView doctorList;
    private List<User> doctors = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        View frame = inflater.inflate(R.layout.fragment_home, container, false);
        tipsList = frame.findViewById(R.id.hpt_list);
        doctorList = frame.findViewById(R.id.hdr_list);
        currentUserName = frame.findViewById(R.id.hcu_name);
        currentUserPhoto = frame.findViewById(R.id.hcu_photo);
        seeAllBtn = frame.findViewById(R.id.hdr_see_all);

        seeAllBtn.setOnClickListener(this);

        updateUI(mAuth.getCurrentUser());
        return frame;
    }

    private void updateUI(FirebaseUser currentUser) {
        showPreventionTips();

        mDatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(currentUserPhoto);
                    currentUserName.setText(mUser.getName());
                    showDoctors(mUser.getThana(), mUser.getDistrict());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showPreventionTips() {
        List<Tips> tips = new ArrayList<>();
        tips.add(new Tips(getString(R.string.tips_mask_wear), R.drawable.gr_mask_wear));
        tips.add(new Tips(getString(R.string.tips_sneeze), R.drawable.gr_sneeze));
        tips.add(new Tips(getString(R.string.tips_handwash), R.drawable.gr_handwash));
        tips.add(new Tips(getString(R.string.tips_cough), R.drawable.gr_cough));
        tips.add(new Tips(getString(R.string.tips_social_distance), R.drawable.gr_social_distance));
        tips.add(new Tips(getString(R.string.tips_touching), R.drawable.gr_touching));
        tips.add(new Tips(getString(R.string.tips_stay_home), R.drawable.gr_stay_home));

        TipsAdapter adapter = new TipsAdapter(tips);
        tipsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tipsList.setHasFixedSize(true);
        tipsList.setAdapter(adapter);
    }

    private void showDoctors(String thana, String district) {
        NearbyDoctorsAdapter adapter = new NearbyDoctorsAdapter(getActivity(), doctors);
        doctorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        doctorList.setHasFixedSize(true);
        doctorList.setAdapter(adapter);

        mDatabase.child("users").orderByChild("doctor/hthana").equalTo(thana).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataThanaSnapshot) {
                doctors.clear();
                int x = 0;
                for (DataSnapshot snapshot : dataThanaSnapshot.getChildren()) {
                    User mUser = snapshot.getValue(User.class);
                    if (mUser!=null) {
                        Doctor mDoctor = mUser.getDoctor();
                        if (mDoctor!=null && !mUser.getId().equals(mAuth.getUid()) && mDoctor.isService()) {
                            if (x>10) break;
                            doctors.add(mUser);
                            adapter.notifyDataSetChanged();
                            x++;
                        }
                    }
                }

                if (x==0) {
                    mDatabase.child("users").orderByChild("doctor/district").equalTo(district).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataDistrictSnapshot) {
                            doctors.clear();
                            int y = 0;
                            for (DataSnapshot snapshot : dataDistrictSnapshot.getChildren()) {
                                User mUser = snapshot.getValue(User.class);
                                if (mUser!=null) {
                                    Doctor mDoctor = mUser.getDoctor();
                                    if (mDoctor!=null && mDoctor.getHdistrict().equals(district) && !mUser.getId().equals(mAuth.getUid()) && mDoctor.isService()) {
                                        if (y>10) break;
                                        doctors.add(mUser);
                                        adapter.notifyDataSetChanged();
                                        y++;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(seeAllBtn)) {
            startActivity(new Intent(getActivity(), DoctorsActivity.class));
        }
    }

    public static class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
        private List<Tips> tips;

        public TipsAdapter(List<Tips> tips) {
            this.tips = tips;
        }

        @NonNull
        @Override
        public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TipsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tips, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
            holder.label.setText(tips.get(position).getLabel());
            holder.graphic.setImageResource(tips.get(position).getGraphic());
        }

        @Override
        public int getItemCount() {
            return tips.size();
        }

        public static class TipsViewHolder extends RecyclerView.ViewHolder{
            private MaterialTextView label;
            private CircularImageView graphic;
            public TipsViewHolder(@NonNull View itemView) {
                super(itemView);
                label = itemView.findViewById(R.id.hpt_label);
                graphic = itemView.findViewById(R.id.hpt_graphic);
            }
        }
    }

    public static class NearbyDoctorsAdapter extends RecyclerView.Adapter<NearbyDoctorsAdapter.NearbyDoctorViewHolder> {
        private Context mContext;
        private List<User> doctors;

        public NearbyDoctorsAdapter(Context context, List<User> doctors) {
            this.mContext = context;
            this.doctors = doctors;
        }

        @NonNull
        @Override
        public NearbyDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new NearbyDoctorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nearby_doctor_x, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull NearbyDoctorViewHolder holder, int position) {
            Picasso.get().load(doctors.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
            holder.name.setText(doctors.get(position).getName());
            Doctor mDoctor = doctors.get(position).getDoctor();
            if (mDoctor!=null) {
                holder.qualification.setText(mDoctor.getQualification());
                if (mDoctor.getRating()!=null) {
                    holder.rating.setRating(Float.parseFloat(mDoctor.getRating()));
                }
            }

            holder.doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, DoctorProfileActivity.class).putExtra("USERID", doctors.get(position).getId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return doctors.size();
        }

        public static class NearbyDoctorViewHolder extends RecyclerView.ViewHolder{
            private RelativeLayout doctor;
            private CircularImageView photo;
            private MaterialTextView name;
            private MaterialTextView qualification;
            private RatingBar rating;
            public NearbyDoctorViewHolder(@NonNull View itemView) {
                super(itemView);
                doctor = itemView.findViewById(R.id.hdr);
                photo = itemView.findViewById(R.id.hdr_photo);
                name = itemView.findViewById(R.id.hdr_name);
                qualification = itemView.findViewById(R.id.hdr_qualification);
                rating = itemView.findViewById(R.id.hdr_rating);
            }
        }
    }
}