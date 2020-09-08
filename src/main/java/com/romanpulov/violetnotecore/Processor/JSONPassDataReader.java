package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory;
import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONPassDataReader extends JSONDataProcessor {

    public PassNote2 readPassNote(JSONObject jo) {
        return new PassNote2(
                jo.optString(PassNote2.ATTR_SYSTEM, null),
                jo.optString(PassNote2.ATTR_USER, null),
                jo.optString(PassNote2.ATTR_PASSWORD, null),
                jo.optString(PassNote2.ATTR_URL, null),
                jo.optString(PassNote2.ATTR_INFO, null),
                parseDate(jo.optString(PassNote2.ATTR_CREATED_DATE, null)),
                parseDate(jo.optString(PassNote2.ATTR_MODIFIED_DATE, null))
        );
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
            byte[] bytes = outputStream.toByteArray();

            JSONObject jo = new JSONObject(new JSONTokener(new String(bytes, StandardCharsets.UTF_8)));
            return readPassData(jo);
        } catch (JSONException | IOException e) {
            throw new DataReadWriteException(e.getMessage());
        }
    }
}
