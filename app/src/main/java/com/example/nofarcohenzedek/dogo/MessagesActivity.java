package com.example.nofarcohenzedek.dogo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.example.nofarcohenzedek.dogo.Model.Utilities;

import java.util.LinkedList;
import java.util.List;

public class MessagesActivity extends Fragment
{
    private ListView list;
    private List<User> data;
    private Boolean isOwner;
    private ProgressBar progressBar;
    private User currentUser;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        final View view = inflater.inflate(R.layout.activity_messages, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        Bundle args = getArguments();
        isOwner = args.getBoolean("isOwner");
        progressBar = (ProgressBar) view.findViewById(R.id.messagesProgressBar);
        data = new LinkedList<>();
        progressBar.setVisibility(View.VISIBLE);
        list = (ListView) view.findViewById(R.id.messagesList);

        final MessagesAdapter adapter = new MessagesAdapter();

        Model.getInstance().getUserById(args.getLong("userId"), new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                currentUser = user;

                if (user instanceof DogWalker) {
                    Model.getInstance().getRequestForDogWalker(user.getId(), new Model.GetDogOwnersListener() {
                        @Override
                        public void onResult(List<DogOwner> dogOwners) {
                            if (!dogOwners.isEmpty()) {
                                for (DogOwner dogOwner : dogOwners) {
                                    data.add(dogOwner);
                                    list.setAdapter(adapter);
                                }
                            } else {
                                ((TextView) view.findViewById(R.id.errorInMessagesList)).setText("אין הודעות להצגה");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Model.getInstance().getRequestOfDogOwner(user.getId(), new Model.GetDogWalkersListener() {
                        @Override
                        public void onResult(List<DogWalker> dogWalkers) {
                            if (!dogWalkers.isEmpty()) {
                                for (DogWalker dogWalker : dogWalkers) {
                                    data.add(dogWalker);
                                    list.setAdapter(adapter);
                                }
                            } else {
                                ((TextView) view.findViewById(R.id.errorInMessagesList)).setText("אין הודעות להצגה");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        return view;
    }

    class MessagesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(currentUser instanceof DogWalker) {
                if (convertView == null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    convertView = inflater.inflate(R.layout.walker_requests_row_layout, null);
                    ImageButton acceptButton = (ImageButton) convertView.findViewById(R.id.acceptButton);
                    ImageButton declineButton = (ImageButton) convertView.findViewById(R.id.declineButton);
                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Model.getInstance().acceptRequest(data.get(position).getId(), currentUser.getId(), new Model.IsSucceedListener() {
                                @Override
                                public void onResult(boolean isSucceed) {
                                    if(isSucceed){
                                        Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "אירעה שגיאה אנה נזה שנית", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Model.getInstance().declineRequest(data.get(position).getId(), currentUser.getId(), new Model.IsSucceedListener() {
                                @Override
                                public void onResult(boolean isSucceed) {
                                    if(isSucceed){
                                        Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "אירעה שגיאה אנה נזה שנית", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }

                TextView dogOwnerNameTV = (TextView) convertView.findViewById(R.id.dogOwnerName);
                TextView dogNameTV = (TextView) convertView.findViewById(R.id.dogName);
                final ImageView dogPic = (ImageView)convertView.findViewById(R.id.csrlImage);

                final DogOwner dogOwner = (DogOwner) data.get(position);
                dogOwnerNameTV.setText(dogOwner.getFirstName() + " " + dogOwner.getLastName());
                dogNameTV.setText(dogOwner.getDog().getName());
                final String picRef = dogOwner.getDog().getPicRef();

                // Get the dog image
                if (picRef != null) {
                    if (Utilities.isFileExistInDevice(picRef)) {
                        Bitmap picture = Utilities.loadImageFromDevice(picRef);

                        if (picture != null) {
                            dogPic.setImageBitmap(picture);
                        }
                    } else {
                        Model.getInstance().getImage(picRef, new Model.GetBitmapListener() {
                            @Override
                            public void onResult(Bitmap picture) {
                                if (picture != null) {
                                    dogPic.setImageBitmap(picture);
                                    Utilities.saveImageOnDevice(picRef, picture);
                                }
                            }
                        });
                    }
                }

                // on click - open the dog owner details
                dogPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), DogOwnerDetails.class);
                        intent.putExtra("dogOwnerId", Long.toString(dogOwner.getId()));
                        startActivity(intent);
                }
                });
            }
            else{
                if (convertView == null) {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    convertView = inflater.inflate(R.layout.owner_requests_row_layout, null);
                }

                TextView dogWalkerNameTV = (TextView) convertView.findViewById(R.id.dogWalkerName);

                DogWalker dogWalker = (DogWalker) data.get(position);
                dogWalkerNameTV.setText(dogWalker.getFirstName() + " " + dogWalker.getLastName());
            }
            return convertView;
        }
    }
}
