package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class JSONPassDataReader {
    public void readStream(InputStream inputStream) throws DataReadWriteException {
        try {
            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
        } catch (Exception e) {

        }
    }
}
