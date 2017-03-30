package com.nupuit.nupuitcontactlist.helper;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.nupuit.nupuitcontactlist.db.Contacts;
import com.nupuit.nupuitcontactlist.db.ContactsDao;
import com.nupuit.nupuitcontactlist.db.DaoMaster;
import com.nupuit.nupuitcontactlist.db.DaoSession;

/**
 * Created by Tushar on 3/31/2017.
 */

public class DBHepler{

    private final String DB_NAME = "nupuit-contact-list-db";

    private final DaoSession daoSession;
    private DaoMaster daoMaster;
    private ContactsDao contactsDao;

    private SQLiteOpenHelper sqLiteOpenHelper;

    /**
     * Constructor of DBHelper
     * @param context Activity Context
     */
    public DBHepler(Context context){
        sqLiteOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        daoMaster = new DaoMaster(sqLiteOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        contactsDao = daoSession.getContactsDao();
    }

    /**
     * insert contacts into local database
     * @param con Contacts object of ContactsDao
     */
    public void insertContact(Contacts con){
        if(getContact(con.getContact_id()) == null){
            contactsDao.insertOrReplaceInTx(con);
        }
    }

    /**
     * get contacts from local database. this methods is used to check if a contact is already exists or not.
     * @param id id of contacts. it is not the row id of local db. it is the original id of main contacts which is come from ContactsContract.CommonDataKinds.Phone._ID
     * @return Contacts object of ContactsDao. it will be null when contacts not found.
     */
    public Contacts getContact(String id) {
        Contacts c = contactsDao.queryBuilder().where(ContactsDao.Properties.Contact_id.eq(id)).unique();
        return c;
    }
}
