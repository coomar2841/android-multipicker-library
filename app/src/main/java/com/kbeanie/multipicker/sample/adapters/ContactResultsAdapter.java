package com.kbeanie.multipicker.sample.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenContact;
import com.kbeanie.multipicker.sample.R;

import java.util.List;

/**
 * Created by kbibek on 2/27/16.
 */
public class ContactResultsAdapter extends BaseAdapter {
    private final Context context;
    private final List<ChosenContact> contacts;

    public ContactResultsAdapter(Context context, List<ChosenContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChosenContact contact = (ChosenContact) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_contacts, null);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        if (contact.getPhotoUri() != null && !contact.getPhotoUri().isEmpty()) {
            Glide.with(context).load(Uri.parse(contact.getPhotoUri())).into(ivImage);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(contact.getDisplayName());

        TextView tvPhones = (TextView) convertView.findViewById(R.id.tvPhones);
        tvPhones.setText(getPhones(contact.getPhones()));

        TextView tvEmails = (TextView) convertView.findViewById(R.id.tvEmails);
        tvEmails.setText(getEmails(contact.getEmails()));
        return convertView;
    }

    private String getEmails(List<String> emails) {
        String emailsString = "";
        for (String email : emails) {
            emailsString += "[" + email + "]";
        }
        return emailsString;
    }

    private String getPhones(List<String> phones) {
        String phonesString = "";
        for (String phone : phones) {
            phonesString += "[" + phone + "]";
        }
        return phonesString;
    }
}
