package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.*;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONPassDataReader extends JSONDataProcessor {

    public PassNote2 readPassNote(JSONObject jo) {
        // core
        PassNote2 passNote2 = new PassNote2(
                jo.optString(PassNote2.ATTR_SYSTEM, null),
                jo.optString(PassNote2.ATTR_USER, null),
                jo.optString(PassNote2.ATTR_PASSWORD, null),
                jo.optString(PassNote2.ATTR_URL, null),
                jo.optString(PassNote2.ATTR_INFO, null),
                parseDate(jo.optString(PassNote2.ATTR_CREATED_DATE, null)),
                parseDate(jo.optString(PassNote2.ATTR_MODIFIED_DATE, null))
        );

        // attributes
        JSONArray ja = jo.optJSONArray(PassNote2.ATTR_ATTRIBUTES, null);
        if (ja != null) {
            List<Attribute> attributes = new ArrayList<>();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jat = ja.optJSONObject(i, null);
                if (jat != null) {
                    String attr_name = jat.optString(Attribute.ATTR_NAME, null);
                    String attr_value = jat.optString(Attribute.ATTR_VALUE, null);
                    if (attr_name != null && attr_value != null) {
                        attributes.add(new Attribute(attr_name, attr_value));
                    }
                }
            }
            if (!attributes.isEmpty()) {
                passNote2.setAttributes(attributes);
            }
        }

        return  passNote2;
    }

    public List<PassNote2> readPassNoteList(JSONArray ja) {
        List<PassNote2> passNoteList = new ArrayList<>(ja.length());

        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            passNoteList.add(readPassNote(jo));
        }

        return passNoteList;
    }

    public PassCategory2 readPassCategory(JSONObject jo) {
        String categoryName = jo.getString(PassCategory2.ATTR_CATEGORY_NAME);
        List<PassNote2> passNoteList = readPassNoteList(jo.getJSONArray(PassCategory2.ATTR_NOTE_LIST));

        return PassCategory2.createWithNotes(categoryName, passNoteList);
    }

    public List<PassCategory2> readPassCategoryList(JSONArray ja) {
        List<PassCategory2> passCategoryList = new ArrayList<>(ja.length());

        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            passCategoryList.add(readPassCategory(jo));

        }

        return passCategoryList;
    }

    public PassData2 readPassData(JSONObject jo) {
        JSONArray ja = jo.getJSONArray(PassData2.ATTR_CATEGORY_LIST);
        return new PassData2(readPassCategoryList(ja));
    }

    public PassData2 readStream(InputStream inputStream) throws DataReadWriteException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // get data as string
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            JSONObject jo = new JSONObject(new JSONTokener(outputStream.toString(StandardCharsets.UTF_8)));
            return readPassData(jo);
        } catch (JSONException | IOException e) {
            throw new DataReadWriteException(e.getMessage());
        }
    }
}
