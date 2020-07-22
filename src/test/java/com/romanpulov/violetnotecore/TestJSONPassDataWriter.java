package com.romanpulov.violetnotecore;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.JSONPassDataWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

public class TestJSONPassDataWriter {

    @Test
    public void testWritePassNotes() {
        PassNote2 passNote1 = new PassNote2("System", "User", "Password", "URL", "Info", null, null);
        PassNote2 passNote2 = new PassNote2("System2", "User2", "Password2", null, null, new Date(), null);

        JSONPassDataWriter writer = new JSONPassDataWriter(null);
        JSONArray ja = writer.writePassNoteList(Arrays.asList(passNote1, passNote2));

        System.out.println(ja);
    }

    @Test
    public void testWritePassCategory() {
        PassNote2 passNote1 = new PassNote2("System", "User", "Password", "URL", "Info", null, null);
        PassNote2 passNote2 = new PassNote2("System2", "User2", "Password2", null, null, new Date(), null);

        PassCategory2 passCategory = new PassCategory2("Category 1");
        passCategory.setNoteList(Arrays.asList(passNote1, passNote2));

        JSONPassDataWriter writer = new JSONPassDataWriter(null);
        JSONObject jo = writer.writePassCategory(passCategory);

        System.out.println(jo);
    }

    @Test
    public void testWritePassData() {
        /*
        PassNote2 passNote1 = new PassNote2("System", "User", "Password", "URL", "Info", null, null);
        PassNote2 passNote2 = new PassNote2("System2", "User2", "Password2", null, null, new Date(), null);
        PassNote2 passNote3 = new PassNote2("System3", "User3", "Password3", null, "Info 3", null, null);

        PassCategory2 passCategory = new PassCategory2("Category 1");
        passCategory.setNoteList(Arrays.asList(passNote1, passNote2));

        PassCategory2 passCategory2 = new PassCategory2("Category 2");
        passCategory2.setNoteList(Arrays.asList(passNote3));

        PassData2 passData = new PassData2();
        passData.setCategoryList(Arrays.asList(passCategory, passCategory2));

         */

        JSONPassDataWriter writer = new JSONPassDataWriter(TestPassData2Generator.generateTestPassData2());
        JSONObject jo = writer.writePassData();

        System.out.println(jo);
    }

    @Test
    public void testWriteAndReadPassData() throws Exception {
        PassData2 passData = TestPassData2Generator.generateTestPassData2();
        JSONPassDataWriter writer = new JSONPassDataWriter(passData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeStream(outputStream);

        byte[] outputBytes = outputStream.toByteArray();
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        String outputString = new String(outputBytes, StandardCharsets.UTF_8);

        System.out.println(outputString);
    }
}
