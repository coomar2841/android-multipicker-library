package com.kbeanie.multipicker.search.api;

import com.kbeanie.multipicker.utils.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 3/1/16.
 */
public class BingSearchApi {
    private final static String BASE_URL = "https://api.datamarket.azure.com/Bing/Search/v1/Image?%24format=json&Query=";

    public List<RemoteImage> getImagesForQuery(String query, String authorizationHeader) throws Exception {
        List<RemoteImage> images = new ArrayList<>();
        URL
                url = new URL(BASE_URL + "%27" + URLEncoder.encode(query, Charset.defaultCharset().name()) + "%27");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", authorizationHeader);
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

        JSONObject json = new JSONObject(IOUtils.convertStreamToString(in));
        JSONArray results = json.getJSONObject("d").getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String urlImage = result.getString("MediaUrl");
            String thumbnail = result.getJSONObject("Thumbnail").getString("MediaUrl");
            String mimeType = result.getString("ContentType");
            RemoteImage image = new RemoteImage();
            image.setUrl(urlImage);
            image.setMimeType(mimeType);
            image.setThumb(thumbnail);
            images.add(image);

        }
        return images;
    }
}
