package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.ContactPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenContact;
import com.kbeanie.multipicker.api.exceptions.PickerException;

/**
 * Created by kbibek on 2/18/16.
 */
public final class ContactPicker extends PickerManager {
    private final static String TAG = ContactPicker.class.getSimpleName();

    private ContactPickerCallback callback;

    public ContactPicker(Activity activity) {
        super(activity, Picker.PICK_CONTACT);
    }

    public ContactPicker(Fragment fragment) {
        super(fragment, Picker.PICK_CONTACT);
    }

    public ContactPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_CONTACT);
    }

    public void setContactPickerCallback(ContactPickerCallback callback) {
        this.callback = callback;
    }

    public void pickContact() {
        try {
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String pick() throws PickerException {
        if (callback == null) {
            throw new PickerException("ContactPickerCallback is null!!! Please set one");
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (extras != null) {
            intent.putExtras(extras);
        }
        pickInternal(intent, Picker.PICK_CONTACT);
        return null;
    }

    @Override
    public void submit(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (data.getData() != null) {
                if (data.getData() instanceof Uri) {
                    Uri uri = data.getData();
                    Log.i(TAG, "submit: " + uri);
                    queryForContact(uri);
                }
            }
        }
    }

    private void queryForContact(Uri uri) {
        String[] projection = {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts.NAME_RAW_CONTACT_ID
        };

        ChosenContact contact = new ChosenContact();

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        int rawContactId = 0;
        if (cursor.moveToFirst()) {
            rawContactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.NAME_RAW_CONTACT_ID));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI));
            contact.setDisplayName(displayName);
            contact.setPhotoUri(photoUri);
        }

        String selection = ContactsContract.Data.RAW_CONTACT_ID + " = ?";
        String[] selectionArgs = {rawContactId + ""};

        Cursor rawContactCursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1
                }, selection, selectionArgs, null);

        try {
            while (rawContactCursor.moveToNext()) {
                String mimeType = rawContactCursor.getString(0);
                String data = rawContactCursor.getString(1);

                if (mimeType.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    contact.setDisplayName(data);
                }
                if (mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    contact.addPhone(data);
                }
                if (mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    contact.addEmail(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rawContactCursor.close();
        }
        cursor.close();
        if (callback != null) {
            callback.onContactChosen(contact);
        }
    }
}
