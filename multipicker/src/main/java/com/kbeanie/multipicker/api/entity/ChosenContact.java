package com.kbeanie.multipicker.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/26/16.
 */
public class ChosenContact implements Parcelable{
    private final static String FORMAT_CONTACT = "Name: %s, Photo: %s, Phones: %s, Emails: %s";
    private String displayName;
    private String photoUri;
    private final List<String> phones;
    private final List<String> emails;
    private int requestId;

    protected ChosenContact(Parcel in) {
        displayName = in.readString();
        photoUri = in.readString();
        phones = in.createStringArrayList();
        emails = in.createStringArrayList();
        requestId = in.readInt();
    }

    public static final Creator<ChosenContact> CREATOR = new Creator<ChosenContact>() {
        @Override
        public ChosenContact createFromParcel(Parcel in) {
            return new ChosenContact(in);
        }

        @Override
        public ChosenContact[] newArray(int size) {
            return new ChosenContact[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(photoUri);
        dest.writeStringList(phones);
        dest.writeStringList(emails);
        dest.writeInt(requestId);
    }
    public ChosenContact() {
        phones = new ArrayList<>();
        emails = new ArrayList<>();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns display name that is available in the phone book
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the photo URI of the contact
     * @return
     */
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }

    /**
     * Returns the phone numbers of the chosen contact
     * @return
     */
    public List<String> getPhones() {
        return phones;
    }

    /**
     * Returns the list of emails of the chosen contact
     * @return
     */
    public List<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    @Override
    public String toString() {
        return String.format(FORMAT_CONTACT, displayName, photoUri, getPhonesString(), getEmailsString());
    }

    private String getEmailsString() {
        String emailsString = "";
        for (String email : emails) {
            emailsString += "[" + email + "]";
        }
        return emailsString;
    }

    private String getPhonesString() {
        String phonesString = "";
        for (String phone : phones) {
            phonesString += "[" + phone + "]";
        }
        return phonesString;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
