package com.partenie.alex.filatelie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.partenie.alex.filatelie.util.CollectionItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    EditText item_name;
    EditText item_description;
    EditText item_price;
    EditText item_manufatured_date;
    EditText item_location;
    Spinner type_spinner;
    Gson gson = new Gson();
    String[] types_from_json;
    ProgressDialog pd;
    private Uri file;
    View photoLayout;
    ImageView preview;
    File image_directory;
    File image = null;
    String myfolder = Environment.getExternalStorageDirectory() + "/CollectionPhotos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        preload();
    }

    private void preload() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        this.getSupportActionBar().setTitle(R.string.add_item_app_bar_title);
        Intent intent = getIntent();
        if (intent.getStringExtra("key") != null) {
            this.getSupportActionBar().setTitle("Edit " + intent.getStringExtra("key"));
        }

        type_spinner = findViewById(R.id.item_type);
        item_name = findViewById(R.id.item_name);
        item_description = findViewById(R.id.item_description);
        item_location = findViewById(R.id.item_location);
        item_price = findViewById(R.id.item_price);
        item_manufatured_date = findViewById(R.id.item_manufatured_date);
        photoLayout = findViewById(R.id.stamp_photo);
        preview = findViewById(R.id.img);
        if (MainActivity.INTERNET) {
            new JsonTask().execute("https://api.myjson.com/bins/iueei");
        } else {
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(getApplicationContext(), R.array.types,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_spinner.setAdapter(adapter);
        }
        photoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });
        image_directory = new File(myfolder);

    }

    private CollectionItem createFromView() {
        Integer id = new Random().nextInt();
        String imgLocation;
        if(image!=null){
            imgLocation = image.getAbsolutePath();
        }else {
            imgLocation="";
        }
        String name = item_name.getText().toString();
        String description = item_description.getText().toString();
        Float price = Float.parseFloat(item_price.getText().toString());
        Date manufacturedDate=null;
        try {
            manufacturedDate = new SimpleDateFormat(DATE_FORMAT, Locale.US)
                    .parse(item_manufatured_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String historicLocation=item_location.getText().toString();
        String type=type_spinner.getSelectedItem().toString();
        return new CollectionItem(id,imgLocation,name,description,price,manufacturedDate,historicLocation,type);
    }


    public void takePicture(View view) {

        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        try {
            image = File.createTempFile(
                    imageFileName,
                    JPEG_FILE_SUFFIX,
                    image_directory
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
                finish();
                return true;
            case R.id.done_action:
//                Toast.makeText(this, item.getItemId() + "", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, createFromView().toString(), Toast.LENGTH_SHORT).show();
                HomeFragment.collectionItems.add(createFromView());
                finish();
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
            type_spinner.setAdapter(adapter);

        }
    }
}
