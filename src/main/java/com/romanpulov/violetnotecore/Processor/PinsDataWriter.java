package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassNote;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteIOException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by rpulov on 22.03.2016.
 */
public class PinsDataWriter {
    private static final String FILE_HEADER = "Category; System; User; Password; Comments; Custom; StartDate; Expires; Info";
    private static final String FILE_DELIMITER = "; ";

    private static String encodeString(String line) {
        return line.trim().replaceAll("\n", "\\|\\|");
    }

    public void writeStream(OutputStream stream, PassData passData) throws DataReadWriteException {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));

            writer.write(FILE_HEADER);
            writer.newLine();

            try {
                for (PassNote passNote : passData.getPassNoteList()) {
                    StringBuilder sb = new StringBuilder();

                    sb.append(encodeString(passNote.getPassCategory().getCategoryName()));
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getSystem()));
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getUser()));
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getPassword()));
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getComments()));
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getCustom()));
                    sb.append(FILE_DELIMITER);

                    //start date
                    sb.append(FILE_DELIMITER);
                    //expires
                    sb.append(FILE_DELIMITER);

                    sb.append(encodeString(passNote.getInfo()));

                    writer.write(sb.toString());
                    writer.newLine();
                }
            } finally {
                writer.flush();
                writer.close();
            }

        } catch (IOException e) {
            throw new DataReadWriteIOException(e.getMessage());
        }
    }
}
