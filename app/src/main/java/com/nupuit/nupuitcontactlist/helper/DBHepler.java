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

    public DBHepler(Context context){
        sqLiteOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        daoMaster = new DaoMaster(sqLiteOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        contactsDao = daoSession.getContactsDao();
    }

    public void insertContact(Contacts con){
        if(getContact(con.getContact_id()) == null){
            contactsDao.insertOrReplaceInTx(con);
        }
    }

    public Contacts getContact(String id) {
        Contacts c = contactsDao.queryBuilder().where(ContactsDao.Properties.Contact_id.eq(id)).unique();
        return c;
    }
}
