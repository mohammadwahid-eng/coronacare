package bd.org.coronacare.main.home;

import android.content.Context;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Tips;
import bd.org.coronacare.models.User;

public class HomeFragment extends Fragment {

    private RecyclerView tipsList;
    private RecyclerView doctorList;
    private List<User> doctors = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_home, container, false);
        tipsList = frame.findViewById(R.id.hpt_list);
        doctorList = frame.findViewById(R.id.hdr_list);
        showPreventionTips();
        showDoctors();
        return frame;
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

    private void showDoctors() {
        NearbyDoctorsAdapter adapter = new NearbyDoctorsAdapter(getActivity(), doctors);
        doctorList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        doctorList.setHasFixedSize(true);
        doctorList.setAdapter(adapter);

        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        doctors.add(new User());
        adapter.notifyDataSetChanged();


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
//            Picasso.get().load(doctors.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
//            holder.name.setText(doctors.get(position).getName());
//            if (doctors.get(position).getDoctor()!=null) {
//                if (doctors.get(position).getDoctor().getQualification()!=null) {
//                    holder.qualification.setText(doctors.get(position).getDoctor().getQualification());
//                }
//                if (doctors.get(position).getDoctor().getRating()!=null) {
//                    holder.rating.setRating(Float.parseFloat(doctors.get(position).getDoctor().getRating()));
//                }
//            }
//
//            holder.item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, DoctorProfileActivity.class);
//                    intent.putExtra("USERID", doctors.get(position).getId());
//                    mContext.startActivity(intent);
//                }
//            });
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
                doctor = itemView.findViewById(R.id.hnd);
                photo = itemView.findViewById(R.id.hnd_photo);
                name = itemView.findViewById(R.id.hnd_name);
                qualification = itemView.findViewById(R.id.hnd_qualification);
                rating = itemView.findViewById(R.id.hnd_rating);
            }
        }
    }
}