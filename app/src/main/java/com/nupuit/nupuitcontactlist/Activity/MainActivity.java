package com.nupuit.nupuitcontactlist.Activity;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.tool.DataBinder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nupuit.nupuitcontactlist.R;
import com.nupuit.nupuitcontactlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        readContactsAndStore();

    }

    /**
     * Read contacts from phone and store in local database
     */
    private void readContactsAndStore(){

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while(phones.moveToNext()){
            String Name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String Number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            String contact_id = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

            Log.e("contacts", "Contact1 : " + Name + ", Number " + Number
                    + ", image_uri " + image_uri + ", id " + contact_id);


            if(image_uri != null){
                //image.setImageURI(Uri.parse(image_uri));
            }


        }
    }
}
