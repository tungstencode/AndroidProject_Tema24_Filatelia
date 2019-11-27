package com.partenie.alex.filatelie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partenie.alex.filatelie.util.CollectionItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddItemActivity extends AppCompatActivity {
    private static final String JPEG_FILE_PREFIX = "pre_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    Intent intent;
    EditText itemName;
    EditText itemDescription;
    EditText itemPrice;
    EditText itemManufaturedDate;
    EditText itemLocation;
    Spinner typeSpinner;
    Gson gson = new Gson();
    String[] types_from_json;
    ProgressDialog pd;
    View photoLayout;
    ImageView preview;
    File imageDirectory;
    File image = null;
    CollectionItem collectionItem = null;
    String photoFolder = Environment.getExternalStorageDirectory() + "/CollectionPhotos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        intent = getIntent();
        preload();
    }

    private void preload() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setTitle(R.string.add_item_app_bar_title);
        if (intent.getStringExtra("key") != null) {
            this.getSupportActionBar().setTitle("Edit " + intent.getStringExtra("key"));
        }

        typeSpinner = findViewById(R.id.item_type);
        itemName = findViewById(R.id.item_name);
        itemDescription = findViewById(R.id.item_description);
        itemLocation = findViewById(R.id.item_location);
        itemPrice = findViewById(R.id.item_price);
        itemManufaturedDate = findViewById(R.id.item_manufatured_date);
        photoLayout = findViewById(R.id.stamp_photo);
        preview = findViewById(R.id.img);
        if (MainActivity.INTERNET) {
            new JsonTask().execute("https://api.myjson.com/bins/iueei");
        } else {
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(getApplicationContext(), R.array.types,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter);
        }
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });
        imageDirectory = new File(photoFolder);

    }

    private CollectionItem createFromView() {
        if (validate()) {
            Integer id = new Random().nextInt();
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
                        .parse(itemManufaturedDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String historicLocation = itemLocation.getText().toString();
            String type = typeSpinner.getSelectedItem().toString();
            return new CollectionItem(id, imgLocation, name, description, price, manufacturedDate, historicLocation, type);
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
        if (itemManufaturedDate.getText().toString() == null || itemPrice.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Incorect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (itemManufaturedDate.getText() == null || itemManufaturedDate.getText().toString().trim().isEmpty() || !validateDate(itemManufaturedDate.getText().toString())) {
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
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
        startActivityForResult(cameraIntent, 100);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this, image.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Bitmap photo = BitmapFactory.decodeFile(image.getAbsolutePath());
            preview.setImageBitmap(photo);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED, intent);
                finish();
                return true;
            case R.id.done_action:
                collectionItem = createFromView();
                if (collectionItem != null) {
                    intent.putExtra("item", collectionItem);
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

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(AddItemActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


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
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            types_from_json = gson.fromJson(result, String[].class);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddItemActivity.this,
                    android.R.layout.simple_spinner_item, types_from_json);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter);

        }
    }
}
