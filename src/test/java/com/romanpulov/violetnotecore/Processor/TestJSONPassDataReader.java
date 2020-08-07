package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.JSONDataProcessor;
import com.romanpulov.violetnotecore.Processor.JSONPassDataReader;
import com.romanpulov.violetnotecore.Processor.JSONPassDataWriter;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassDataTools;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJSONPassDataReader {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static String formatDate(Date date) {
        return date == null ? null : df.format(date);
    }

    @Test
    public void testDateParser() {
        Date d1 = new Date();
        String formattedDate = formatDate(d1);
        System.out.println("Formatted date:" + formattedDate);

        try {
            Date d2 = df.parse(formattedDate);
            String formattedParsedDate = formatDate(d2);
            System.out.println("Parsed date:   " + formattedParsedDate);

            assertEquals(formattedDate, formattedParsedDate);

            System.out.println(d1.toString());
            System.out.println(d2.toString());

            assertEquals(d1.toString(), d2.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testJSONDataProcessor() {
        Date d1 = new Date();
        String formattedD1 = JSONDataProcessor.formatDate(d1);
        Date d2 = JSONPassDataReader.parseDate(formattedD1);
        assertEquals(d1.toString(), d2.toString());
        assertNull(JSONPassDataReader.parseDate(null));
        assertNull(JSONPassDataReader.parseDate("sss"));
    }

    @Test
    public void testWriteAndReadPassData() throws Exception {
        PassData2 passData = TestPassData2Generator.generateTestPassData2();
        JSONPassDataWriter writer = new JSONPassDataWriter(passData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeStream(outputStream);

        byte[] outputBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);

        JSONPassDataReader reader = new JSONPassDataReader();

        PassData2 readPassData = reader.readStream(inputStream);

        assertNotNull(readPassData);
        assertEquals(passData.getCategoryList().size(), readPassData.getCategoryList().size());

        String dataEqualResult = TestPassDataTools.passDataEquals(passData, readPassData);
        if (dataEqualResult != null) {
            fail(dataEqualResult);
        }

    }

}
