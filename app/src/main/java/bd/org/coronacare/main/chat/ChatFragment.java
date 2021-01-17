package bd.org.coronacare.main.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.org.coronacare.R;
import bd.org.coronacare.main.chat.conversation.ConversationActivity;
import bd.org.coronacare.models.Chat;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DateTimeFormat;
import bd.org.coronacare.utils.DividerItemDecorator;

public class ChatFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView chatUserList;
    private ChatsAdapter adapter;

    private ShimmerFrameLayout shimmerChatUsers;

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
        shimmerChatUsers = frame.findViewById(R.id.shmrchtu_list);
        showChatUsers();
        return frame;
    }

    private void showChatUsers() {
        Query query = mDatabase.child("messages").child(mAuth.getUid());
        FirebaseRecyclerOptions<Map> options =
                new FirebaseRecyclerOptions.Builder<Map>()
                    .setQuery(query, new SnapshotParser<Map>() {
                        @NonNull
                        @Override
                        public Map parseSnapshot(@NonNull DataSnapshot dataSnapshot) {
                            Map mMap = new HashMap();
                            mMap.put("key", dataSnapshot.getKey());

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Chat chat = snapshot.getValue(Chat.class);
                                if (chat!=null) {
                                    mMap.put("chat", chat);
                                }
                            }
                            return mMap;
                        }
                    })
                    .build();

        adapter = new ChatsAdapter(options);
        chatUserList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatUserList.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.gr_line_horizontal)));
        chatUserList.setHasFixedSize(true);
        chatUserList.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                shimmerChatUsers.stopShimmer();
                shimmerChatUsers.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private static class ChatsAdapter extends FirebaseRecyclerAdapter<Map, ChatsAdapter.ChatViewHolder> {

        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;

        public ChatsAdapter(@NonNull FirebaseRecyclerOptions<Map> options) {
            super(options);
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
            return new ChatsAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_user, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Map model) {
            Chat chat = (Chat) model.get("chat");
            if (chat.getSender().equals(mAuth.getUid())) {
                if (chat.getType().equals("text")) {
                    holder.message.setText("You: " + chat.getMessage());
                } else {
                    holder.message.setText("You sent an attachment");
                }
            } else {
                if (chat.getType().equals("text")) {
                    holder.message.setText(chat.getMessage());
                } else {
                    holder.message.setText("You received an attachment");
                }
            }
            holder.time.setText(DateTimeFormat.getChatTime(chat.getTime()));
            holder.user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), ConversationActivity.class).putExtra("USERID", model.get("key").toString()));
                }
            });

            mDatabase.child("users").child(model.get("key").toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User mUser = snapshot.getValue(User.class);
                    if (mUser!=null) {
                        Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.photo);
                        holder.activity.setColorFilter(mUser.isOnline() ? Color.rgb(49, 162, 76) : Color.rgb(200, 200, 200));
                        holder.name.setText(mUser.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public static class ChatViewHolder extends RecyclerView.ViewHolder{
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