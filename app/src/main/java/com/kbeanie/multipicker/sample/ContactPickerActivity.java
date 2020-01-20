package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        lvResults = (ListView) findViewById(R.id.lvResults);
    }

    private ContactPicker picker;

    private void selectSingleContact() {
        picker = getContactPicker();
        picker.pickContact();
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
                picker.submit(data);
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
