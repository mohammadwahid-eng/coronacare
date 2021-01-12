package bd.org.coronacare.main.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.main.chat.conversation.ConversationActivity;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DividerItemDecorator;

public class ChatFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView chatUserList;
    private List<User> users = new ArrayList<>();
    private ChatsAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        chatUserList = frame.findViewById(R.id.chtu_list);
        showChatUsers();
        return frame;
    }

    private void showChatUsers() {
        adapter = new ChatsAdapter(getActivity(), users);
        chatUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatUserList.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.gr_line_horizontal)));
        chatUserList.setHasFixedSize(true);
        chatUserList.setAdapter(adapter);

        mDatabase.child("messages").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataKeySnapshot) {
                for (DataSnapshot snapshot : dataKeySnapshot.getChildren()) {
                    loadChatUser(snapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadChatUser(String userID) {
        mDatabase.child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user!=null) {
                    users.add(user);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>{

        private Context mContext;
        private List<User> users;

        public ChatsAdapter(Context mContext, List<User> users) {
            this.mContext = mContext;
            this.users = users;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ChatsAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_user, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            Picasso.get().load(users.get(position).getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
            holder.activity.setColorFilter(users.get(position).isOnline() ? Color.rgb(49, 162, 76) : Color.rgb(200, 200, 200));
            holder.name.setText(users.get(position).getName());
            holder.user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ConversationActivity.class).putExtra("USERID", users.get(position).getId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public static class ChatViewHolder extends RecyclerView.ViewHolder {
            private RelativeLayout user;
            private CircularImageView photo;
            private CircularImageView activity;
            private MaterialTextView name;
            private MaterialTextView message;
            private MaterialTextView time;
            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                user        = itemView.findViewById(R.id.chtu);
                photo       = itemView.findViewById(R.id.chtu_photo);
                activity    = itemView.findViewById(R.id.chtu_activity);
                name        = itemView.findViewById(R.id.chtu_name);
                message     = itemView.findViewById(R.id.chtu_message);
                time        = itemView.findViewById(R.id.chtu_time);
            }
        }
    }
}