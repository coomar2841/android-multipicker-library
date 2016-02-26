package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kbeanie.multipicker.api.ContactPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ContactPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenContact;
import com.kbeanie.multipicker.sample.adapters.ContactResultsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class ContactPickerActivity extends AbActivity implements ContactPickerCallback {
    private ListView lvResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker_activity);

        getSupportActionBar().setTitle("Contact Picker");
        getSupportActionBar().setSubtitle("Activity example");

        Button btContactSingle = (Button) findViewById(R.id.btContactSingle);
        btContactSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSingleContact();
            }
        });

        Button btContactMultiple = (Button) findViewById(R.id.btContactMultiple);
        btContactMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMultipleContacts();
            }
        });

        lvResults = (ListView) findViewById(R.id.lvResults);
    }

    private ContactPicker picker;

    private void selectSingleContact() {
        picker = getContactPicker();
        picker.pick();
    }

    private void selectMultipleContacts() {
        picker = getContactPicker();
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        picker.setExtras(extras);
        picker.pick();
    }

    private ContactPicker getContactPicker() {
        ContactPicker picker = new ContactPicker(this);
        picker.setContactPickerCallback(this);
        return picker;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_CONTACT) {
                picker.submit(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onContactChosen(ChosenContact contact) {
        List<ChosenContact> contacts = new ArrayList<>();
        contacts.add(contact);
        ContactResultsAdapter adapter = new ContactResultsAdapter(this, contacts);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {

    }
}
