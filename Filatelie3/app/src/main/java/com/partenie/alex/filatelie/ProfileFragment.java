package com.partenie.alex.filatelie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "AndroidClarified";
    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    private View profile;
    private TextView profileName;
    private Button logOut;
    private ImageView profileImage;
    private TextView nrOfStamps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nrOfStamps=view.findViewById(R.id.nr_of_stamps);
        if(HomeFragment.collectionItems!=null){
            nrOfStamps.setText(String.valueOf(HomeFragment.collectionItems.size()));
        }else{
            nrOfStamps.setText("0");
        }

        profileImage = view.findViewById(R.id.profile_image);
        logOut = view.findViewById(R.id.log_out);
        profile = view.findViewById(R.id.profile);
        profileName = view.findViewById(R.id.profile_name);
        googleSignInButton = view.findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("603668152714-4nag6u7c9oj5dlbirj8fa5r8oo9ec2f7.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 505);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sharedpreferences.edit().putBoolean("loggedin", false).apply();
                profile.setVisibility(View.GONE);
                googleSignInButton.setVisibility(View.VISIBLE);
                googleSignInClient.signOut();
            }
        });

        if (MainActivity.sharedpreferences.getBoolean("loggedin", false)) {
            profile.setVisibility(View.VISIBLE);
            googleSignInButton.setVisibility(View.GONE);
            profileName.setText(MainActivity.sharedpreferences.getString("name", "Guest"));
            profileImage.setImageBitmap(loadImageBitmap(getContext(), "my_image.jpeg"));
            Log.println(Log.INFO, "someti", "din shared");

        } else {
            profile.setVisibility(View.GONE);
            googleSignInButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getContext(), result, "my_image.jpeg");
            profileImage.setImageBitmap(loadImageBitmap(getContext(), "my_image.jpeg"));
        }
    }


    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream = context.openFileInput(imageName);
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Toast.makeText(getContext(), "Logged in", Toast.LENGTH_LONG).show();
        googleSignInButton.setVisibility(View.GONE);
        profile.setVisibility(View.VISIBLE);
        profileName.setText(googleSignInAccount.getDisplayName());
        googleSignInAccount.getPhotoUrl();
        new DownloadImage().execute(googleSignInAccount.getPhotoUrl().toString());
        SharedPreferences.Editor editor = MainActivity.sharedpreferences.edit();
        editor.putBoolean("loggedin", true);
        editor.putString("name", googleSignInAccount.getDisplayName());
        editor.commit();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 505:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }

    }


}
