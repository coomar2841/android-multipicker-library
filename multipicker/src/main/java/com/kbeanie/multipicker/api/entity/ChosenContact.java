package com.kbeanie.multipicker.api.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/26/16.
 */
public class ChosenContact {
    private final static String FORMAT_CONTACT = "Name: %s, Photo: %s, Phones: %s, Emails: %s";
    private String displayName;
    private String photoUri;
    private List<String> phones;
    private List<String> emails;

    public ChosenContact() {
        phones = new ArrayList<>();
        emails = new ArrayList<>();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }

    public List<String> getPhones() {
        return phones;
    }

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
}
