package com.partenie.alex.filatelie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.partenie.alex.filatelie.database.model.CollectionItem;
import com.partenie.alex.filatelie.database.service.CollectionItemService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

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
    private TextView nrOfMonezi;
    private TextView nrOfFDC;
    private TextView nrOver100;
    private static final int GOOGLE_CODE = 505;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nrOfStamps = view.findViewById(R.id.nr_of_stamps);
        nrOfMonezi = view.findViewById(R.id.nr_of_monezi);
        nrOfFDC = view.findViewById(R.id.nr_of_fdc);
        nrOver100 = view.findViewById(R.id.nr_over_100);
        if (HomeFragment.collectionItems != null) {
            nrOfStamps.setText(String.valueOf(HomeFragment.collectionItems.size()));
            nrOfMonezi.setText(getString(R.string.zero));
            nrOfFDC.setText(getString(R.string.zero));
            nrOver100.setText(getString(R.string.zero));
            getTypeFromDatabase("Moneda", view, nrOfMonezi);
            getTypeFromDatabase("FDC", view, nrOfFDC);
            getOverValue(100f, view, nrOver100);

        } else {
            nrOfStamps.setText(getString(R.string.zero));
            nrOver100.setText(getString(R.string.zero));
            nrOfMonezi.setText(getString(R.string.zero));
            nrOfFDC.setText(getString(R.string.zero));
        }

        profileImage = view.findViewById(R.id.profile_image);
        logOut = view.findViewById(R.id.log_out);
        profile = view.findViewById(R.id.profile);
        profileName = view.findViewById(R.id.profile_name);
        googleSignInButton = view.findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_CODE);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.sharedpreferences.edit().putBoolean(getString(R.string.LOGIN_KEY), false).apply();
                profile.setVisibility(View.GONE);
                googleSignInButton.setVisibility(View.VISIBLE);
                googleSignInClient.signOut();
            }
        });

        if (MainActivity.sharedpreferences.getBoolean(getString(R.string.LOGIN_KEY), false)) {
            profile.setVisibility(View.VISIBLE);
            googleSignInButton.setVisibility(View.GONE);
            profileName.setText(MainActivity.sharedpreferences.getString(getString(R.string.NAME_KEY), getString(R.string.NAME_DEFAULT)));
            profileImage.setImageBitmap(loadImageBitmap(getContext(), getString(R.string.profile_picture)));
        } else {
            profile.setVisibility(View.GONE);
            googleSignInButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getContext(), result, getString(R.string.profile_picture));
            profileImage.setImageBitmap(loadImageBitmap(getContext(), getString(R.string.profile_picture)));
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
            e.printStackTrace();
        }
        return bitmap;
    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        googleSignInButton.setVisibility(View.GONE);
        profile.setVisibility(View.VISIBLE);
        profileName.setText(googleSignInAccount.getDisplayName());
        googleSignInAccount.getPhotoUrl();
        new DownloadImage().execute(googleSignInAccount.getPhotoUrl().toString());
        SharedPreferences.Editor editor = MainActivity.sharedpreferences.edit();
        editor.putBoolean(getString(R.string.LOGIN_KEY), true);
        editor.putString(getString(R.string.NAME_KEY), googleSignInAccount.getDisplayName());
        editor.commit();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GOOGLE_CODE:
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            onLoggedIn(account);
                        }
                    } catch (ApiException e) {
                    }
                    break;
            }

    }

    @SuppressLint("StaticFieldLeak")
    private void getTypeFromDatabase(final String tip, final View view, final TextView tv) {
        new CollectionItemService.GetType(view.getContext()) {
            @Override
            protected void onPostExecute(
                    List<CollectionItem> results) {
                if (results != null) {
                    tv.setText(String.valueOf(results.size()));
                    try {
                        File file = new File(Environment.getExternalStorageDirectory()+"/type"+tip+".txt");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(results.toString());
                        bw.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(tip);
    }

    @SuppressLint("StaticFieldLeak")
    private void getOverValue(final Float value, final View view, final TextView tv) {
        new CollectionItemService.GetOver(view.getContext()) {
            @Override
            protected void onPostExecute(
                    List<CollectionItem> results) {
                if (results != null) {
                    tv.setText(String.valueOf(results.size()));
                    try {
                        File file = new File(Environment.getExternalStorageDirectory()+"/over"+value+".txt");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(results.toString());
                        bw.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(value);
    }
}
