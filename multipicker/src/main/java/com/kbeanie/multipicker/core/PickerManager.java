package com.kbeanie.multipicker.core;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.utils.FileUtils;

import java.io.File;
import java.util.UUID;

import storage.StoragePreferences;

/**
 * Abstract class for all types of Pickers
 */
public abstract class PickerManager {
    private final static String TAG = PickerManager.class.getSimpleName();
    protected Activity activity;
    protected Fragment fragment;
    protected android.app.Fragment appFragment;

    protected final int pickerType;

    protected int requestId;

    protected int cacheLocation = CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR;

    protected Bundle extras;

    protected boolean allowMultiple;

    public PickerManager(Activity activity, int pickerType) {
        this.activity = activity;
        this.pickerType = pickerType;
    }

    public PickerManager(Fragment fragment, int pickerType) {
        this.fragment = fragment;
        this.pickerType = pickerType;
    }

    public PickerManager(android.app.Fragment appFragment, int pickerType) {
        this.appFragment = appFragment;
        this.pickerType = pickerType;
    }

    /**
     * Set extras which will be directly passed to the target applications. You should use this
     * to take advantage of specific applications
     * ex. Some applications support cropping, or editing the image itself before they give you
     * the final image
     *
     * @param extras
     */
    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    /**
     * Default cache location is {@link CacheLocation#EXTERNAL_STORAGE_APP_DIR}
     * <p/>
     * If you are setting the (@link CacheLocation#EXTERNAL_STORAGE_PUBLIC_DIR} make sure you have the required permissions
     * available in the Manifest file. Else, a {@link RuntimeException} will be raised.
     * <p/>
     * Permissions required {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} and
     * {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}
     *
     * @param cacheLocation {@link CacheLocation}
     */
    public void setCacheLocation(int cacheLocation) {
        this.cacheLocation = cacheLocation;

        if (cacheLocation == CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR) {
            checkIfPermissionsAvailable();
        }
    }

    public void setFolderName(String folderName) {
        StoragePreferences preferences = new StoragePreferences(getContext());
        preferences.setFolderName(folderName);
    }

    /**
     * Triggers pick image
     *
     * @return
     */
    protected abstract String pick() throws PickerException;

    /**
     * This method should be called after {@link Activity#onActivityResult(int, int, Intent)} is  called.
     *
     * @param data
     */
    public abstract void submit(Intent data);

    protected String buildFilePath(String extension, String type) throws PickerException {
        String directoryPath = getDirectory(type);
        return directoryPath + File.separator + UUID.randomUUID().toString() + "." + extension;
    }

    protected String getDirectory(String type) throws PickerException {
        String directory = null;
        switch (cacheLocation) {
            case CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR:
                directory = FileUtils.getExternalFilesDirectory(type, getContext());
                break;
            case CacheLocation.EXTERNAL_STORAGE_APP_DIR:
                directory = FileUtils.getExternalFilesDir(type, getContext());
                break;
            case CacheLocation.EXTERNAL_CACHE_DIR:
                directory = FileUtils.getExternalCacheDir(getContext());
                break;
            case CacheLocation.INTERNAL_APP_DIR:
                directory = FileUtils.getInternalFileDirectory(getContext());
                break;
        }
        return directory;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getActivity();
        } else if (appFragment != null) {
            return appFragment.getActivity();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void pickInternal(Intent intent, int type) {
        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }
        if (activity != null) {
            activity.startActivityForResult(intent, type);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, type);
        } else if (appFragment != null) {
            appFragment.startActivityForResult(intent, type);
        }
    }

    protected boolean isClipDataApi() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    private void checkIfPermissionsAvailable() {
        boolean writePermissionInManifest = getContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "checkIfPermissionsAvailable: In Manifest(WRITE_EXTERNAL_STORAGE): " + writePermissionInManifest);
        boolean readPermissionInManifest = getContext().checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "checkIfPermissionsAvailable: In Manifest(READ_EXTERNAL_STORAGE): " + readPermissionInManifest);

        if (!writePermissionInManifest || !readPermissionInManifest) {
            if (!writePermissionInManifest) {
                Log.e(TAG, Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission is missing in manifest file");
            }
            if (!readPermissionInManifest) {
                Log.e(TAG, Manifest.permission.READ_EXTERNAL_STORAGE + " permission is missing in manifest file");
            }
            throw new RuntimeException("Permissions required in Manifest");
        }
    }

    public static long querySizeOfFile(Uri uri, Context context) {
        if (uri.toString().startsWith("file")) {
            File file = new File(uri.getPath());
            return file.length();
        } else if (uri.toString().startsWith("content")) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                } else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            } finally {
                cursor.close();
            }
        }
        return 0;
    }


    protected String getNewFileLocation(String extension, String type) throws PickerException {
        File file;
        String filePathName = "";
        if (type.equals(Environment.DIRECTORY_MOVIES)) {
            filePathName = "movies";
        } else if (type.equals(Environment.DIRECTORY_PICTURES)) {
            filePathName = "pictures";
        }
        file = new File(getContext().getFilesDir(), filePathName);
        file.mkdirs();

        file = new File(file.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + "." + extension);
        return file.getAbsolutePath();
    }

    protected String getFileProviderAuthority(){
        return getContext().getPackageName()+".multipicker.fileprovider";
    }
}
