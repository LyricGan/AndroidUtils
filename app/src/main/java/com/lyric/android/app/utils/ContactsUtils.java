package com.lyric.android.app.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyricgan
 * @description 联系人工具类
 * @time 2015/11/12 16:10
 */
public class ContactsUtils {
    private static ContactsUtils mInstance;
    private Context mContext;

    private ContactsUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static synchronized ContactsUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ContactsUtils(context);
        }
        return mInstance;
    }

    /**
     * 读取手机联系人列表
     * @return List<ContactsEntity>
     */
    public List<ContactsEntity> readContactsList() {
        if (!checkContactPermission(mContext)) {
            return null;
        }
        List<ContactsEntity> contactsEntityList = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex;
        int nameIndex;
        if (cursor != null && cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            ContactsEntity contactsEntity;
            Cursor phoneCursor = null;
            while (cursor.moveToNext()) {
                String contactId = cursor.getString(contactIdIndex);
                String name = cursor.getString(nameIndex);
                // 查找联系人的电话信息
                phoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
                int phoneIndex;
                if (phoneCursor != null && phoneCursor.getCount() > 0) {
                    phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    // 用户可能存在多个电话号码
                    while (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneIndex);

                        contactsEntity = new ContactsEntity();
                        contactsEntity.setId(contactId);
                        contactsEntity.setName(name);
                        contactsEntity.setPhoneNumber(phoneNumber);

                        contactsEntityList.add(contactsEntity);
                    }
                } else {
                    // 用户只存了名字
                    contactsEntity = new ContactsEntity();
                    contactsEntity.setId(contactId);
                    contactsEntity.setName(name);
                    contactsEntity.setPhoneNumber("");

                    contactsEntityList.add(contactsEntity);
                }
            }
            if (phoneCursor != null && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactsEntityList;
    }

    /**
     * 读取手机联系人电话号码列表
     * @return List<String>
     */
    public List<String> readContactsMobileList() {
        if (!checkContactPermission(mContext)) {
            return null;
        }
        List<String> phoneNumberList = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.TYPE + "=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, null, null);
        int phoneIndex;
        if (cursor != null && cursor.getCount() > 0) {
            phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(phoneIndex);

                phoneNumberList.add(phoneNumber);
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return phoneNumberList;
    }

    /**
     * 检查是否获得读取联系人的权限
     * @param context Context
     * @return boolean
     */
    private boolean checkContactPermission(Context context) {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 联系人实体类
     */
    public class ContactsEntity {
        /**
         * 联系人ID
         */
        private String id;
        /**
         * 联系人名称
         */
        private String name;
        /**
         * 联系人手机号
         */
        private String phoneNumber;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
