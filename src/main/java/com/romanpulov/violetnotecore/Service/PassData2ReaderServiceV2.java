package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Converter.PassData2Converter;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV1;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PassData2ReaderServiceV2 {
    public static PassData2 fromStream(InputStream inputStream, String password)
            throws AESCryptException, IOException, DataReadWriteException
    {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            bufferedInputStream.mark(10);

            return fromBufferedStream(bufferedInputStream, password);
        }
    }

    public static PassData2 fromBufferedStream(BufferedInputStream bufferedInputStream, String password)
            throws AESCryptException, IOException, DataReadWriteException {
        try {
            FilePassDataReaderV2 readerV2 = new FilePassDataReaderV2(bufferedInputStream, password);
            return readerV2.readFile();
        } catch (DataReadWriteException e2) {
            try {
                bufferedInputStream.reset();
            } catch (IOException e21) {
                throw e2;
            }

            FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(bufferedInputStream, password);
            PassData passData = readerV1.readFile();
            return PassData2Converter.from(passData);
        }
    }
}
