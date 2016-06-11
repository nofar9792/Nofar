package com.example.nofarcohenzedek.dogo;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofarcohenzedek.dogo.Model.DogOwner;
import com.example.nofarcohenzedek.dogo.Model.DogWalker;
import com.example.nofarcohenzedek.dogo.Model.Model;
import com.example.nofarcohenzedek.dogo.Model.Request;
import com.example.nofarcohenzedek.dogo.Model.User;
import com.example.nofarcohenzedek.dogo.Model.Utilities;

import java.util.LinkedList;
import java.util.List;

public class MessagesFragment extends Fragment
{
    private ListView list;
    private List<Request> data;
    private Boolean isOwner;
    private Long userId;
    private ProgressBar progressBar;
    private User currentUser;
    private Context context;

    public MessagesFragment(){}

    public MessagesFragment(Long id, boolean IsOwner)
    {
        userId = id;
        isOwner = IsOwner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        final View view = inflater.inflate(R.layout.activity_messages, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        progressBar = (ProgressBar) view.findViewById(R.id.messagesProgressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        data = new LinkedList<>();
        progressBar.setVisibility(View.VISIBLE);
        list = (ListView) view.findViewById(R.id.messagesList);

        final MessagesAdapter adapter = new MessagesAdapter();

        Model.getInstance().getUserById(userId, new Model.GetUserListener() {
            @Override
            public void onResult(User user) {
                currentUser = user;

                if (user instanceof DogWalker) {
                    Model.getInstance().getRequestsByDogWalkerId(user.getId(), new Model.GetRequestsListener() {
                        @Override
                        public void onResult(List<Request> requests) {
                            if (!requests.isEmpty()) {
                                data = requests;
                                list.setAdapter(adapter);

                            } else {
                                ((TextView) view.findViewById(R.id.errorInMessagesList)).setText("אין הודעות להצגה");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Model.getInstance().getRequestsByDogOwnerId(user.getId(), new Model.GetRequestsListener() {
                        @Override
                        public void onResult(List<Request> requests) {
                            if (!requests.isEmpty()) {
                                data = requests;
                                list.setAdapter(adapter);
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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            if (isOwner) {
                if (data.get(position).isOwnerAskedWalker()) {
                    return createRequestingMessage(inflater, position);

                } else {
                    return createRequestedMessage(inflater, position);
                }
            } else {
                if (data.get(position).isOwnerAskedWalker()) {
                    return createRequestedMessage(inflater, position);

                } else {
                    return createRequestingMessage(inflater, position);
                }
            }
        }

        private View createRequestingMessage(LayoutInflater inflater, int position) {
            View convertView = inflater.inflate(R.layout.owner_requests_row_layout, null);
            TextView userFullNameTV = (TextView) convertView.findViewById(R.id.requestingUser);

            final User requestUser;

            if(isOwner){
                requestUser = data.get(position).getDogWalker();
            }else {
                requestUser = data.get(position).getDogOwner();
            }

            userFullNameTV.setText(requestUser.getFirstName() + " " + requestUser.getLastName());

            return convertView;
        }

        private View createRequestedMessage(LayoutInflater inflater, final int position) {
            View convertView = inflater.inflate(R.layout.walker_requests_row_layout, null);
            ImageButton acceptButton = (ImageButton) convertView.findViewById(R.id.acceptButton);
            ImageButton declineButton = (ImageButton) convertView.findViewById(R.id.declineButton);

            setAcceptButton(acceptButton, position);
            setDeclineButton(declineButton, position);

            TextView userFullNameTV = (TextView) convertView.findViewById(R.id.requestingUser);
            final ImageView detailsImage = (ImageView) convertView.findViewById(R.id.csrlImage);

            final User requestUser;

            if(isOwner){
                requestUser = data.get(position).getDogWalker();
            }else {
                requestUser = data.get(position).getDogOwner();
            }

            userFullNameTV.setText(requestUser.getFirstName() + " " + requestUser.getLastName());

            if(requestUser instanceof DogOwner){
                setDogPic(detailsImage, requestUser);
            }

            setDetailsImageButton(detailsImage, requestUser);

            return convertView;
        }

        private void setAcceptButton(ImageButton acceptButton, final int position) {
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOwner){
                        Model.getInstance().acceptRequest(currentUser.getId(),data.get(position).getDogWalkerId(), new Model.IsSucceedListener() {
                            @Override
                            public void onResult(boolean isSucceed) {
                                if(isSucceed){
                                    Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,"אירעה שגיאה, אנא נסה שנית" ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Model.getInstance().acceptRequest(data.get(position).getDogOwnerId(), currentUser.getId(), new Model.IsSucceedListener() {
                            @Override
                            public void onResult(boolean isSucceed) {
                                if(isSucceed){
                                    Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,"אירעה שגיאה, אנא נסה שנית" ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        private void setDeclineButton(ImageButton declineButton, final int position) {
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOwner){
                        Model.getInstance().declineRequest(currentUser.getId(), data.get(position).getDogWalkerId(), new Model.IsSucceedListener() {
                            @Override
                            public void onResult(boolean isSucceed) {
                                if (isSucceed) {
                                    Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context,"אירעה שגיאה, אנא נסה שנית" ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Model.getInstance().declineRequest(data.get(position).getDogOwnerId(), currentUser.getId(), new Model.IsSucceedListener() {
                            @Override
                            public void onResult(boolean isSucceed) {
                                if(isSucceed){
                                    Toast.makeText(context, "ההודעה נשלחה בהצלחה", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context,"אירעה שגיאה, אנא נסה שנית" ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        private void setDogPic(final ImageView detailsImage, final User requestUser) {
            final String picRef = ((DogOwner)requestUser).getDog().getPicRef();

            // Get the dog image
            if (picRef != null) {
                if (Utilities.isFileExistInDevice(picRef)) {
                    Bitmap picture = Utilities.loadImageFromDevice(picRef);

                    if (picture != null) {
                        detailsImage.setImageBitmap(picture);
                    }
                } else {
                    Model.getInstance().getImage(picRef, new Model.GetBitmapListener() {
                        @Override
                        public void onResult(Bitmap picture) {
                            if (picture != null) {
                                detailsImage.setImageBitmap(picture);
                                Utilities.saveImageOnDevice(picRef, picture);
                            }
                        }
                    });
                }
            }
        }

        private void setDetailsImageButton(ImageView detailsImage, final User requestUser) {
            detailsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (currentUser instanceof DogWalker) {
                        intent = new Intent(getActivity().getApplicationContext(), DogOwnerDetailsActivity.class);
                        intent.putExtra("ownerId", requestUser.getId());
                        intent.putExtra("walkerId", userId);
                    } else {
                        intent = new Intent(getActivity().getApplicationContext(), DogWalkerDetailsActivity.class);
                        intent.putExtra("walkerId", requestUser.getId());
                        intent.putExtra("ownerId", userId);
                    }
                    startActivity(intent);
                }
            });
        }
    }
}
