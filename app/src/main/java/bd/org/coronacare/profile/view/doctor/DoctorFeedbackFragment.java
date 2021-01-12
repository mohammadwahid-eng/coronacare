package bd.org.coronacare.profile.view.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

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
import bd.org.coronacare.models.Feedback;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DateTimeFormat;
import bd.org.coronacare.utils.DividerItemDecorator;

public class DoctorFeedbackFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView feedbackList;
    private String doctorID;
    private List<Feedback> feedbacks = new ArrayList<>();
    private FeedbackAdapter adapter;

    public DoctorFeedbackFragment() {
        // Required empty public constructor
    }

    public DoctorFeedbackFragment(String doctorID) {
        this.doctorID = doctorID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_doctor_feedback, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        feedbackList = frame.findViewById(R.id.dpf_list);
        loadFeedbacks();
        return frame;
    }

    private void loadFeedbacks() {
        adapter = new FeedbackAdapter(feedbacks);
        feedbackList.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedbackList.setHasFixedSize(true);
        feedbackList.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.gr_line_horizontal)));
        feedbackList.setAdapter(adapter);

        mDatabase.child("users").child(doctorID).child("doctor").child("feedbacks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot feedbackSnapshot) {
                for(DataSnapshot snapshot : feedbackSnapshot.getChildren()) {
                    Feedback mFeedback = snapshot.getValue(Feedback.class);
                    if (mFeedback!=null) {
                        feedbacks.add(mFeedback);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

        private DatabaseReference mDatabase;
        private List<Feedback> feedbacks;

        public FeedbackAdapter(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
        }

        @NonNull
        @Override
        public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedbackAdapter.FeedbackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feedback, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
            holder.comment.setText(feedbacks.get(position).getComment());
            holder.rating.setRating(feedbacks.get(position).getScore());
            holder.time.setText(DateTimeFormat.feedbackTime(feedbacks.get(position).getTime()));
            mDatabase.child("users").child(feedbacks.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User mUser = snapshot.getValue(User.class);
                    if (mUser!=null) {
                        Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
                        holder.name.setText(mUser.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return feedbacks.size();
        }

        public static class FeedbackViewHolder extends RecyclerView.ViewHolder{
            private CircularImageView photo;
            private MaterialTextView name;
            private RatingBar rating;
            private MaterialTextView time;
            private MaterialTextView comment;

            public FeedbackViewHolder(@NonNull View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.dpfu_photo);
                name = itemView.findViewById(R.id.dpfu_name);
                rating = itemView.findViewById(R.id.dpfu_rating);
                time = itemView.findViewById(R.id.dpfu_time);
                comment = itemView.findViewById(R.id.dpfu_comment);
            }
        }
    }
}