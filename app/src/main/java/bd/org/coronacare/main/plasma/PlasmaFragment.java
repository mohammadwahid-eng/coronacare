package bd.org.coronacare.main.plasma;

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
import android.widget.RelativeLayout;

import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DividerItemDecorator;

public class PlasmaFragment extends Fragment {

    private RecyclerView donorList;
    private List<User> donors = new ArrayList<>();
    private DonorAdapter adapter;


    public PlasmaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_plasma, container, false);
        donorList = frame.findViewById(R.id.plsmd_list);
        showDonors();
        return frame;
    }

    private void showDonors() {
        adapter = new DonorAdapter(getActivity(), donors);
        donorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        donorList.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.gr_line_horizontal)));
        donorList.setHasFixedSize(true);
        donorList.setAdapter(adapter);

        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        donors.add(new User());
        adapter.notifyDataSetChanged();
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
//            Picasso.get().load(donors.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
//            holder.name.setText(donors.get(position).getName());
//            holder.mobile.setText(donors.get(position).getMobile());
//            holder.donor.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, UserProfileActivity.class);
//                    intent.putExtra("USERID", donors.get(position).getId());
//                    mContext.startActivity(intent);
//                }
//            });
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