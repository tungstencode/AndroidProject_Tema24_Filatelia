package com.partenie.alex.filatelie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partenie.alex.filatelie.database.model.CollectionItem;
import com.partenie.alex.filatelie.util.Country;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity {
    private static final String JPEG_FILE_PREFIX = "filatelie_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final int CAMERA_CODE=100;
    Intent intent;
    EditText itemName;
    EditText itemDescription;
    EditText itemPrice;
    EditText itemManufacturedDate;
    EditText itemLocation;
    Spinner typeSpinner;
    Gson gson = new Gson();
    ProgressDialog pd;
    View photoLayout;
    ImageView preview;
    File imageDirectory;
    File image = null;
    File imageSaved = null;
    CollectionItem collectionItem = null;
    Button getLocationInfo;
    TextView countryInfo;
    String photoFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        intent = getIntent();
        preload();
    }

    private void updateUI() {
        itemName.setText(collectionItem.name);
        itemManufacturedDate.setText(new SimpleDateFormat(DATE_FORMAT).format(collectionItem.manufacturedDate));
        itemPrice.setText(collectionItem.price.toString());
        itemLocation.setText(collectionItem.historicLocation);
        itemDescription.setText(collectionItem.description);
        if (collectionItem.imgLocation.isEmpty()) {
            preview.setImageResource(R.drawable.ic_add_black_24dp);
        } else {
            Bitmap photo = BitmapFactory.decodeFile(collectionItem.imgLocation);
            preview.setImageBitmap(photo);
            image = new File(collectionItem.imgLocation);
            imageSaved = image;
        }
        typeSpinner.setSelection(((ArrayAdapter<CharSequence>) typeSpinner.getAdapter()).getPosition(collectionItem.type));


    }

    private void preload() {
        photoFolder = Environment.getExternalStorageDirectory() + getString(R.string.FOLDER_NAME);
        typeSpinner = findViewById(R.id.item_type);
        itemName = findViewById(R.id.item_name);
        itemDescription = findViewById(R.id.item_description);
        itemLocation = findViewById(R.id.item_location);
        itemPrice = findViewById(R.id.item_price);
        itemManufacturedDate = findViewById(R.id.item_manufatured_date);
        photoLayout = findViewById(R.id.stamp_photo);
        countryInfo = findViewById(R.id.country_info);
        preview = findViewById(R.id.img);
        getLocationInfo = findViewById(R.id.get_location_info);
        getLocationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryInfo.setVisibility(View.VISIBLE);
                new JsonTask().execute(getString(R.string.API_BODY) + itemLocation.getText().toString());
            }
        });

        populateFromXML();
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });
        imageDirectory = new File(photoFolder);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setTitle(R.string.add_item_app_bar_title);
        if (intent.getStringExtra(getString(R.string.COLLECTION_ITEM_EDIT)) != null) {
            this.getSupportActionBar().setTitle("Edit " + intent.getStringExtra(getString(R.string.COLLECTION_ITEM_EDIT)));
            collectionItem = intent.getParcelableExtra(String.valueOf(R.string.EDIT_ITEM_KEY));
            getLocationInfo.setVisibility(View.VISIBLE);
            updateUI();
        }
    }

    private CollectionItem createFromView() {
        if (validate()) {
            String imgLocation;
            if (image != null) {
                imgLocation = image.getAbsolutePath();
            } else {
                imgLocation = "";
            }
            String name = itemName.getText().toString();
            String description = itemDescription.getText().toString();
            Float price = Float.parseFloat(itemPrice.getText().toString());
            Date manufacturedDate = null;
            try {
                manufacturedDate = new SimpleDateFormat(DATE_FORMAT, Locale.US)
                        .parse(itemManufacturedDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String historicLocation = itemLocation.getText().toString();
            String type = typeSpinner.getSelectedItem().toString();
            if(collectionItem!=null){
                Log.d("itemFromCreateFromView","id: "+collectionItem.id);
                return new CollectionItem(collectionItem.id, imgLocation, name, description, price, manufacturedDate, historicLocation, type);
            }else{
                return new CollectionItem(imgLocation, name, description, price, manufacturedDate, historicLocation, type);
            }
        }
        return null;
    }

    private boolean validate() {
        if (itemName.getText().toString() == null || itemName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemDescription.getText().toString() == null || itemDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemLocation.getText().toString() == null || itemLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemPrice.getText().toString() == null || itemPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemManufacturedDate.getText().toString() == null || itemPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemManufacturedDate.getText() == null || itemManufacturedDate.getText().toString().trim().isEmpty() || !validateDate(itemManufacturedDate.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Incorect date", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateDate(String strDate) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(DATE_FORMAT, Locale.US);
        try {
            simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void takePicture(View view) {

        String timeStamp =
                new SimpleDateFormat(getString(R.string.PHOTO_DATA_FORMAT)).format(new Date());

        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        try {
            image = File.createTempFile(
                    imageFileName,
                    JPEG_FILE_SUFFIX,
                    imageDirectory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(cameraIntent, CAMERA_CODE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap photo = BitmapFactory.decodeFile(image.getAbsolutePath());
            preview.setImageBitmap(photo);
        } else {
            image = imageSaved;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED, intent);
                if (intent.getStringExtra(getString(R.string.COLLECTION_ITEM_EDIT)) == null) {
                    if (image != null) {
                        if (image.isFile() && image.exists()) {
                            image.delete();
                        }
                    }
                }
                finish();
                return true;
            case R.id.done_action:
                collectionItem = createFromView();
                if (collectionItem != null) {
                    intent.putExtra(getString(R.string.COLLETION_ITEM_KEY), collectionItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_tick_menu, menu);
        return true;
    }

    private void populateFromXML() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getApplicationContext(), R.array.types,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddItemActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            if (isOnline()) {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(params[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    return buffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (result == null) {
                countryInfo.setText(R.string.COUNTRY_ERROR);
            } else {
                Country country[] = gson.fromJson(result, Country[].class);
                String builder = country[0].name + "/" + country[0].nativeName + "\n" +
                        "Capital: " + country[0].capital + ", Region: " + country[0].subregion + "\n" +
                        "Currency: " + country[0].currencies[0].name + "\n" +
                        "Language: " + country[0].languages[0].name + "\n" +
                        "Population: " + country[0].population;
                countryInfo.setText(builder);
            }
        }
    }
}
