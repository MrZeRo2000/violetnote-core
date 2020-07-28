package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassCategory2;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Model.PassNote2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteIOException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONPassDataWriter extends JSONDataProcessor {
    private final PassData2 passData;

    public JSONPassDataWriter(PassData2 passData) {
        this.passData = passData;
    }

    public JSONObject writePassNote(PassNote2 passNote) {
        return new JSONObject()
                .putOpt(PassNote2.ATTR_SYSTEM, passNote.getSystem())
                .putOpt(PassNote2.ATTR_USER, passNote.getUser())
                .putOpt(PassNote2.ATTR_PASSWORD, passNote.getPassword())
                .putOpt(PassNote2.ATTR_URL, passNote.getUrl())
                .putOpt(PassNote2.ATTR_INFO, passNote.getInfo())
                .putOpt(PassNote2.ATTR_CREATED_DATE, formatDate(passNote.getCreatedDate()))
                .putOpt(PassNote2.ATTR_MODIFIED_DATE, formatDate(passNote.getModifiedDate()))
                ;
    }

    public JSONArray writePassNoteList(List<PassNote2> passNoteList) {
        JSONArray ja = new JSONArray();

        for (PassNote2 passNote: passNoteList) {
            ja.put(writePassNote(passNote));
        }

        return ja;
    }

    public JSONObject writePassCategory(PassCategory2 passCategory) {
        return new JSONObject()
                .put(PassCategory2.ATTR_CATEGORY_NAME, passCategory.getCategoryName())
                .put(PassCategory2.ATTR_NOTE_LIST, writePassNoteList(passCategory.getNoteList()))
                ;
    }

    public JSONArray writePassCategoryList(List<PassCategory2> passCategoryList) {
        JSONArray ja = new JSONArray();

        for (PassCategory2 passCategory: passCategoryList) {
            ja.put(writePassCategory(passCategory));
        }

        return ja;
    }

    public JSONObject writePassData() {
        return new JSONObject()
            .put(PassData2.ATTR_CATEGORY_LIST, writePassCategoryList(passData.getCategoryList()));
    }

    public void writeStream(OutputStream stream) throws DataReadWriteException {
        try {
            stream.write(writePassData().toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new DataReadWriteIOException(e.getMessage());
        }
    }
}
