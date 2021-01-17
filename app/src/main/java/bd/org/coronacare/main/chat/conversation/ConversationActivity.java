package bd.org.coronacare.main.chat.conversation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Chat;
import bd.org.coronacare.models.User;
import bd.org.coronacare.profile.view.DoctorProfileActivity;
import bd.org.coronacare.utils.DateTimeFormat;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_PHOTO_REQUEST = 111;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private String partnerID;
    private String partnerMobileNo;
    private MaterialToolbar toolbar;
    private TextInputEditText writtenMessage;
    private ImageView photoBtn;
    private ImageView sendBtn;
    private RecyclerView conversations;
    private ConversationAdapter adapter;
    private ProgressDialog preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mStorage = FirebaseStorage.getInstance().getReference();

        preLoader = new ProgressDialog(this, R.style.AppTheme_ProgressDialog);
        preLoader.setCanceledOnTouchOutside(false);

        toolbar = findViewById(R.id.toolbar);
        writtenMessage = findViewById(R.id.conv_message);
        photoBtn = findViewById(R.id.conv_photo_btn);
        sendBtn = findViewById(R.id.conv_send_btn);
        conversations = findViewById(R.id.conv_list);
        partnerID = getIntent().getStringExtra("USERID");

        toolbar.inflateMenu(R.menu.menu_conversation);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menc_call) {
                    callPartner();
                } else if (item.getItemId() == R.id.menc_info) {
                    showProfile();
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

        photoBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        fetchUserMeta();
        fetchConversations();
    }

    private void fetchUserMeta() {
        mDatabase.child("users").child(partnerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    toolbar.setTitle(mUser.getName());
                    toolbar.setSubtitle(mUser.isOnline() ? "Active Now" : DateTimeFormat.getTimeAgo(mUser.getLastOnline()));
                    if (mUser.getDoctor()!=null) {
                        partnerMobileNo = mUser.getDoctor().getHmobile();
                    } else {
                        partnerMobileNo = mUser.getMobile();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchConversations() {

        Query query = mDatabase.child("messages").child(mAuth.getUid()).child(partnerID);
        FirebaseRecyclerOptions<Chat> options =
                new FirebaseRecyclerOptions.Builder<Chat>()
                        .setQuery(query, new SnapshotParser<Chat>() {
                            @NonNull
                            @Override
                            public Chat parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Chat chat = new Chat();
                                chat.setId(snapshot.child("id").getValue().toString());
                                chat.setMessage(snapshot.child("message").getValue().toString());
                                chat.setType(snapshot.child("type").getValue().toString());
                                chat.setSender(snapshot.child("sender").getValue().toString());
                                chat.setReceiver(snapshot.child("receiver").getValue().toString());
                                chat.setTime(Long.parseLong(snapshot.child("time").getValue().toString()));
                                return chat;
                            }
                        })
                        .build();

        adapter = new ConversationAdapter(options);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                conversations.smoothScrollToPosition(positionStart);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        conversations.setLayoutManager(layoutManager);
        conversations.setHasFixedSize(true);
        conversations.setAdapter(adapter);
    }

    private void sendMessage() {
        final String message = writtenMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            writtenMessage.setText("");
            String messageID = mDatabase.child("messages").child(mAuth.getUid()).child(partnerID).push().getKey();

            Map chat = new HashMap();
            chat.put("id", messageID);
            chat.put("message", message);
            chat.put("type", "text");
            chat.put("sender", mAuth.getUid());
            chat.put("receiver", partnerID);
            chat.put("time", ServerValue.TIMESTAMP);

            Map messageMap = new HashMap();
            messageMap.put("messages/" + mAuth.getUid() + "/" + partnerID + "/" + messageID + "", chat);
            messageMap.put("messages/" + partnerID + "/" + mAuth.getUid() + "/" + messageID + "", chat);
            mDatabase.updateChildren(messageMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (!task.isSuccessful()) {
                        writtenMessage.setText(message);
                        Toast.makeText(ConversationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    private void sendPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"), PICK_PHOTO_REQUEST);
    }

    private void uploadPhoto(Bitmap bitmap) {
        if (bitmap==null) {
            return;
        }
        preLoader.show();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        mStorage.child("messages").child(mAuth.getUid() + System.currentTimeMillis() + ".jpg").putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        preLoader.dismiss();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String messageID = mDatabase.child("messages").child(mAuth.getUid()).child(partnerID).push().getKey();

                                Map chat = new HashMap();
                                chat.put("id", messageID);
                                chat.put("message", uri.toString());
                                chat.put("type", "image");
                                chat.put("sender", mAuth.getUid());
                                chat.put("receiver", partnerID);
                                chat.put("time", ServerValue.TIMESTAMP);

                                Map messageMap = new HashMap();
                                messageMap.put("messages/" + mAuth.getUid() + "/" + partnerID + "/" + messageID + "", chat);
                                messageMap.put("messages/" + partnerID + "/" + mAuth.getUid() + "/" + messageID + "", chat);
                                mDatabase.updateChildren(messageMap);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        preLoader.dismiss();
                        Toast.makeText(ConversationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                preLoader.setMessage("Uploading: " + (int)progress + "%");
            }
        });
    }

    private void callPartner() {
        if (!partnerMobileNo.isEmpty()) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + partnerMobileNo)));
        }
    }

    private void showProfile() {
        startActivity(new Intent(ConversationActivity.this, DoctorProfileActivity.class).putExtra("USERID", partnerID));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(photoBtn)) {
            sendPhoto();
        } else if (v.equals(sendBtn)) {
            sendMessage();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ) {

            Bitmap bitmap = null;
            try {
                if (android.os.Build.VERSION.SDK_INT >= 29){
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), data.getData()));
                } else{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadPhoto(bitmap);
        }
    }

    private void setStatus(boolean online) {
        if (mAuth.getUid()!=null) {
            mDatabase.child("users").child(mAuth.getUid()).child("online").setValue(online);
            mDatabase.child("users").child(mAuth.getUid()).child("lastOnline").setValue(ServerValue.TIMESTAMP);
        }
    }

    private static class ConversationAdapter extends FirebaseRecyclerAdapter<Chat, ConversationAdapter.ChatViewHolder> {
        private static int CHAT_TYPE_LEFT_ATTACHMENT = 1;
        private static int CHAT_TYPE_LEFT_MESSAGE = 2;
        private static int CHAT_TYPE_RIGHT_ATTACHMENT = 3;
        private static int CHAT_TYPE_RIGHT_MESSAGE = 4;
        private FirebaseAuth mAuth;
        private DatabaseReference mDatabase;

        public ConversationAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
            super(options);
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.keepSynced(true);
        }

        @Override
        public int getItemViewType(int position) {
            Chat chat = getItem(position);
            if (chat.getSender().equals(mAuth.getUid())) {
                //Right
                if (chat.getType().equals("text")) {
                    return CHAT_TYPE_RIGHT_MESSAGE;
                } else {
                    return CHAT_TYPE_RIGHT_ATTACHMENT;
                }
            } else {
                //Left
                if (chat.getType().equals("text")) {
                    return CHAT_TYPE_LEFT_MESSAGE;
                } else {
                    return CHAT_TYPE_LEFT_ATTACHMENT;
                }
            }
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == CHAT_TYPE_RIGHT_MESSAGE) {
                return new ConversationAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_message, parent, false));
            } else if (viewType == CHAT_TYPE_RIGHT_ATTACHMENT) {
                return new ConversationAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_right_attachment, parent, false));
            } else if (viewType == CHAT_TYPE_LEFT_MESSAGE) {
                return new ConversationAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_message, parent, false));
            } else if (viewType == CHAT_TYPE_LEFT_ATTACHMENT) {
                return new ConversationAdapter.ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_left_attachment, parent, false));
            }
            return null;
        }

        @Override
        protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
            if (getItemViewType(position) == CHAT_TYPE_RIGHT_MESSAGE) {
                holder.rightMessage.setText(model.getMessage());
            } else if (getItemViewType(position) == CHAT_TYPE_RIGHT_ATTACHMENT) {
                Picasso.get().load(model.getMessage()).into(holder.rightAttachment);
            } else if (getItemViewType(position) == CHAT_TYPE_LEFT_MESSAGE) {
                holder.leftMessage.setText(model.getMessage());

                mDatabase.child("users").child(model.getSender()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User mUser = snapshot.getValue(User.class);
                        if (mUser!=null) {
                            Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.userPhotoLeftMessage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else if (getItemViewType(position) == CHAT_TYPE_LEFT_ATTACHMENT) {
                Picasso.get().load(model.getMessage()).into(holder.leftAttachment);
                mDatabase.child("users").child(model.getSender()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User mUser = snapshot.getValue(User.class);
                        if (mUser!=null) {
                            Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(holder.userPhotoLeftAttachment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

        public static class ChatViewHolder extends RecyclerView.ViewHolder{
            private ImageView userPhotoLeftMessage;
            private ImageView userPhotoLeftAttachment;
            private MaterialTextView rightMessage;
            private ImageView rightAttachment;
            private MaterialTextView leftMessage;
            private ImageView leftAttachment;
            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                rightMessage = itemView.findViewById(R.id.convr_message);
                rightAttachment = itemView.findViewById(R.id.convra_photo);
                leftMessage = itemView.findViewById(R.id.convl_message);
                leftAttachment = itemView.findViewById(R.id.convla_photo);
                userPhotoLeftMessage = itemView.findViewById(R.id.convl_uphoto);
                userPhotoLeftAttachment = itemView.findViewById(R.id.convla_uphoto);
            }
        }
    }
}