package com.nupuit.nupuitcontactlist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nupuit.nupuitcontactlist.R;
import com.nupuit.nupuitcontactlist.databinding.RowContactListBinding;
import com.nupuit.nupuitcontactlist.db.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tushar on 3/31/2017.
 */

public class MainAdapter extends BaseAdapter{

    private List<Contacts> contacts;
    private Context context;

    public MainAdapter(List<Contacts> contacts, Context context){
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

        Holder holder;

        if(convertView == null){
            holder = new Holder();
            convertView = holder.binding.getRoot();
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.binding.tvName.setText(contacts.get(position).getName());
        holder.binding.tvNumber.setText(contacts.get(position).getNumber());

        return convertView;
    }

    private class Holder{

        RowContactListBinding binding;

        public Holder(){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_contact_list, null, true);
        }
    }
}
