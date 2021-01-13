package bd.org.coronacare.main.plasma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import bd.org.coronacare.profile.view.UserProfileActivity;
import bd.org.coronacare.utils.DividerItemDecorator;

public class PlasmaFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView donorList;
    private List<User> donors = new ArrayList<>();
    private DonorAdapter adapter;

    private FloatingActionButton fab;
    private ShimmerFrameLayout shimmerDonors;


    public PlasmaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        View frame = inflater.inflate(R.layout.fragment_plasma, container, false);
        donorList = frame.findViewById(R.id.plsmd_list);
        fab = frame.findViewById(R.id.plsmd_fab);
        shimmerDonors = frame.findViewById(R.id.shmrpd_list);
        fab.setOnClickListener(this);
        showDonors();
        return frame;
    }

    private void showDonors() {
        adapter = new DonorAdapter(getActivity(), donors);
        donorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        donorList.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.gr_line_horizontal)));
        donorList.setHasFixedSize(true);
        donorList.setAdapter(adapter);

        mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fab.setVisibility(View.VISIBLE);
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    loadDonorList(mUser.getBgroup(), mUser.getThana(), mUser.getDistrict());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDonorList(String bgroup, String thana, String district) {
        donors.clear();
        adapter.notifyDataSetChanged();
        mDatabase.child("users").orderByChild("thana").equalTo(thana).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataThanaSnapshot) {
                shimmerDonors.stopShimmer();
                shimmerDonors.setVisibility(View.GONE);
                for (DataSnapshot snapshot : dataThanaSnapshot.getChildren()) {
                    User mUser = snapshot.getValue(User.class);
                    if (mUser!=null && mUser.getBgroup().equals(bgroup) && mUser.isDonor() && !mUser.getId().equals(mAuth.getUid())) {
                        Doctor mDoctor = mUser.getDoctor();
                        if (mDoctor!=null) {
                            if (mDoctor.getHdistrict().equals(district)) {
                                donors.add(mUser);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            if (mUser.getDistrict().equals(district)) {
                                donors.add(mUser);
                                adapter.notifyDataSetChanged();
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
            startActivityForResult(new Intent(getActivity(), PlasmaDonorSearchActivity.class), 100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data!=null) {
            loadDonorList(data.getStringExtra("BGROUP"), data.getStringExtra("THANA"), data.getStringExtra("DISTRICT"));
        }
    }

    public static class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {

        private Context mContext;
        private List<User> donors;

        public DonorAdapter(Context mContext, List<User> donors) {
            this.mContext = mContext;
            this.donors = donors;
        }

        @NonNull
        @Override
        public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DonorAdapter.DonorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_donor, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
            Picasso.get().load(donors.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
            holder.name.setText(donors.get(position).getName());
            holder.mobile.setText(donors.get(position).getMobile());
            holder.donor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserProfileActivity.class);
                    intent.putExtra("USERID", donors.get(position).getId());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return donors.size();
        }

        public static class DonorViewHolder extends RecyclerView.ViewHolder{
            private RelativeLayout donor;
            private CircularImageView photo;
            private MaterialTextView name;
            private MaterialTextView mobile;

            public DonorViewHolder(@NonNull View itemView) {
                super(itemView);
                donor = itemView.findViewById(R.id.plsmd);
                photo = itemView.findViewById(R.id.plsmd_photo);
                name = itemView.findViewById(R.id.plsmd_name);
                mobile = itemView.findViewById(R.id.plsmd_mobile);
            }
        }
    }
}