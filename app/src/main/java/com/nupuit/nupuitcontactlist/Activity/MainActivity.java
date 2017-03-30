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
import com.nupuit.nupuitcontactlist.adapter.MainAdapter;
import com.nupuit.nupuitcontactlist.databinding.ActivityMainBinding;
import com.nupuit.nupuitcontactlist.db.Contacts;
import com.nupuit.nupuitcontactlist.helper.DBHepler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private int LOAD_AMOUNT = 10;
    private int loadCount = 1;

    private ActivityMainBinding binding;

    private DBHepler dbHepler;

    private List<Contacts> contacts;
    private List<Contacts> contactsLn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        contactsLn = new ArrayList<>();

        dbHepler = new DBHepler(this);

        readContactsAndStore();

        loadListView(0, 1000);

    }

    /**
     * Read contacts from phone and store in local database
     */
    private void readContactsAndStore(){

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while(phones.moveToNext()){
            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            String contact_id = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

            Log.e("contacts", "Contact1 : " + name + ", Number " + number
                    + ", image_uri " + image_uri + ", id " + contact_id);

            //init contacts
            Contacts c = new Contacts();
            c.setName(name);
            c.setNumber(number);
            c.setContact_id(contact_id);
            c.setUri(image_uri);

            //insert contacts in dataabse
            dbHepler.insertContact(c);

        }
    }

    /**
     * get all contacts from local database and add it into list
     */
    private void getContacts(){
        contacts = dbHepler.getContactsAsList();
    }

    /**
     * load the listview of contacts list
     * @param start starting index
     * @param range ending index
     */
    private void loadListView(int start, int range){

        if(contacts != null){
            for(int i = start; i < range; i++){
                if(i < contacts.size()){
                    contactsLn.add(contacts.get(i));
                }else {
                    break;
                }
            }
            binding.contentMain.lvContactList.setAdapter(new MainAdapter(contactsLn, this));
        }else {
            getContacts();
            loadListView(start, range);
        }


    }
}
