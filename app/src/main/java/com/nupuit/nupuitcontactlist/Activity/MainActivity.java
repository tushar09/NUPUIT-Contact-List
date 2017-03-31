package com.nupuit.nupuitcontactlist.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.nupuit.nupuitcontactlist.R;
import com.nupuit.nupuitcontactlist.adapter.MainAdapter;
import com.nupuit.nupuitcontactlist.databinding.ActivityMainBinding;
import com.nupuit.nupuitcontactlist.databinding.FooterViewBinding;
import com.nupuit.nupuitcontactlist.db.Contacts;
import com.nupuit.nupuitcontactlist.helper.DBHepler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private int LOAD_AMOUNT = 10;
    private int loadCount = 0;

    private ActivityMainBinding binding;
    private FooterViewBinding bindingFooter;

    private MainAdapter mainAdapter;

    private DBHepler dbHepler;

    private List<Contacts> contacts;
    private List<Contacts> contactsLn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        contactsLn = new ArrayList<>();

        mainAdapter = new MainAdapter(contactsLn, this);

        dbHepler = new DBHepler(this);

        bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.footer_view, null, true);
        bindingFooter.btLoadMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(bindingFooter.btLoadMore.getText().toString().equalsIgnoreCase("No more results to show")){
                    Toast.makeText(MainActivity.this, "No more results to show", Toast.LENGTH_SHORT).show();
                }else {
                    loadCount++;
                    int start = (loadCount * LOAD_AMOUNT) + 1;
                    int range = start + LOAD_AMOUNT;
                    updateList(start, range);
                }

            }
        });

        binding.contentMain.lvContactList.addFooterView(bindingFooter.getRoot());

        haveReadContactPermission();

    }

    /**
     * check if this app has the permission to access the contacts or not.
     * @return boolean
     */
    public boolean haveReadContactPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                readContactsAndStore();
                loadListView(0, LOAD_AMOUNT);
                return true;
            } else {
                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            readContactsAndStore();
            loadListView(0, LOAD_AMOUNT);
            return true;
        }
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
            binding.contentMain.lvContactList.setAdapter(mainAdapter);
            if(contactsLn.size() == contacts.size()){
                bindingFooter.btLoadMore.setText("No more results to show");
            }
        }else {
            getContacts();
            loadListView(start, range);
        }

    }

    /**
     * load more item to the listview of contacts list
     * @param start starting index
     * @param range ending index
     */
    private void updateList(int start, int range){
        for(int i = start; i < range; i++){
            if(i < contacts.size()){
                contactsLn.add(contacts.get(i));
            }else {
                bindingFooter.btLoadMore.setText("No more results to show");
                break;
            }
        }
        mainAdapter.notifyDataSetChanged();
        if(contactsLn.size() == contacts.size() || contactsLn.size() >= contacts.size()){
            bindingFooter.btLoadMore.setText("No more results to show");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //you have the permission now.
            readContactsAndStore();
            loadListView(0, LOAD_AMOUNT);
        }
    }
}
