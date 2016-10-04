package com.kbeanie.multipicker.api;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.ContactPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenContact;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.PickerManager;

/**
 * Choose a contact from your address book
 */
public final class ContactPicker extends PickerManager {
    private final static String TAG = ContactPicker.class.getSimpleName();

    private ContactPickerCallback callback;

    /**
     * Constructor for choosing a contact from an {@link Activity}
     *
     * @param activity
     */
    public ContactPicker(Activity activity) {
        super(activity, Picker.PICK_CONTACT);
    }

    /**
     * Constructor for choosing a contact from a {@link Fragment}
     *
     * @param fragment
     */
    public ContactPicker(Fragment fragment) {
        super(fragment, Picker.PICK_CONTACT);
    }

    /**
     * Constructor for choosing a contact from a {@link android.app.Fragment}
     *
     * @param appFragment
     */
    public ContactPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_CONTACT);
    }

    /**
     * Listener which gets callbacks when the choosen contact is ready to be used
     *
     * @param callback
     */
    public void setContactPickerCallback(ContactPickerCallback callback) {
        this.callback = callback;
    }

    /**
     * Initiate the Contact Chooser.
     * <p>
     * Make sure you have {@link android.Manifest.permission#READ_CONTACTS} in your Manifest file.
     * <p>
     * Else a {@link RuntimeException} will be raised.
     */
    public void pickContact() {
        try {
            checkPermission();
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    protected String pick() throws PickerException {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (extras != null) {
            intent.putExtras(extras);
        }
        pickInternal(intent, Picker.PICK_CONTACT);
        return null;
    }

    /**
     * Call this method from
     * {@link Activity#onActivityResult(int, int, Intent)}
     * OR
     * {@link Fragment#onActivityResult(int, int, Intent)}
     * OR
     * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
     *
     * @param data
     */
    @Override
    public void submit(Intent data) {
        if (data != null) {
            if (data.getData() != null) {
                if (data.getData() instanceof Uri) {
                    Uri uri = data.getData();
                    Log.d(TAG, "submit: " + uri);
                    queryForContact(uri);
                }
            }
        }
    }

    private int getRawContactId(int contactId) {
        int rawContactId;
        String[] projection = {ContactsContract.RawContacts._ID};
        String selection = ContactsContract.RawContacts.CONTACT_ID + " = ?";
        String[] selectionArgs = {contactId + ""};
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
        cursor.moveToFirst();
        rawContactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.RawContacts._ID));
        cursor.close();
        return rawContactId;
    }

    private void queryForContact(Uri uri) {
        String[] projection = {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts._ID
        };

        ChosenContact contact = new ChosenContact();
        contact.setRequestId(requestId);

        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        int contactId = 0;
        if (cursor.moveToFirst()) {
            contactId = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            String photoUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI));
            contact.setDisplayName(displayName);
            contact.setPhotoUri(photoUri);
        }

        int rawContactId = getRawContactId(contactId);

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
        try {
            if (callback != null) {
                callback.onContactChosen(contact);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        boolean permissionGranted = getContext().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "checkIfPermissionsAvailable: In Manifest(READ_CONTACTS): " + permissionGranted);
        if (!permissionGranted) {
            Log.e(TAG, Manifest.permission.READ_CONTACTS + " permission is missing in manifest file");
            throw new RuntimeException("Permissions missing in Manifest");
        }
    }
}
