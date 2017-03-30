package com.nupuit.nupuitcontactlist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nupuit.nupuitcontactlist.db.Contacts;

import java.util.ArrayList;

/**
 * Created by Tushar on 3/31/2017.
 */

public class MainAdapter extends BaseAdapter{

    private ArrayList<Contacts> contacts;
    private Context context;

    public MainAdapter(ArrayList<Contacts> contacts, Context context){
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount(){
        return contacts.size();
    }

    @Override
    public Object getItem(int position){
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return null;
    }

    private class Holder{

    }
}
