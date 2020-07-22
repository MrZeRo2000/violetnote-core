package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassNote2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestJSONSerialization {

    @Test
    public void testJSONPassNote2() {
        PassNote2 passNote = new PassNote2("System", "User", "Password", "URL", "Info", null, null);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passNote", passNote);

        System.out.println(jsonObject.toString());

        JSONArray ja = new JSONArray();
        ja.put(passNote);

        System.out.println(ja.toString());

        JSONObject jo1 = new JSONObject();
        jo1.put("system", "System");
        jo1.put("user", "User");

        JSONObject jo2 = new JSONObject();
        jo2.put("system", "System2");
        jo2.put("user", "User2");
        jo2.put("url", "URL2");

        JSONArray ja12 = new JSONArray();
        ja12.put(jo1);
        ja12.put(jo2);

        System.out.println(ja12.toString());
    }

    @Test
    public void testStringToJSON() {
        String json = "{\"categories\":[{\"notes\":[{\"password\":\"Password 1\",\"system\":\"System 1\",\"user\":\"User 1\",\"url\":\"URL 1\",\"info\":\"Info 1\"},{\"password\":\"Password 2\",\"system\":\"System 2\",\"user\":\"User 2\",\"url\":\"URL 2\",\"info\":\"Info 2\"}],\"name\":\"Category 1\"},{\"notes\":[{\"password\":\"Password 3\",\"system\":\"System 3\",\"user\":\"User 3\",\"url\":\"URL 3\",\"info\":\"Info 3\"}],\"name\":\"Category 2\"}]}";
        JSONObject jo = new JSONObject(json);
        JSONArray ja = jo.optJSONArray("categories");
        assertNotNull(ja);

        JSONArray ja1 = jo.optJSONArray("categories1");
        assertNull(ja1);
    }
}
