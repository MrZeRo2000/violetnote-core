package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataReaderV3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PassData2ReaderServiceV3 {
    public static PassData2 fromStream(InputStream inputStream, String password)
    throws AESCryptException, IOException, DataReadWriteException
    {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            bufferedInputStream.mark(10);

            try {
                FilePassDataReaderV3 readerV3 = new FilePassDataReaderV3(bufferedInputStream, password);
                return readerV3.readFile();
            } catch (DataReadWriteException e3) {
                bufferedInputStream.reset();

                return PassData2ReaderServiceV2.fromBufferedStream(bufferedInputStream, password);
            }
        }
    }
}
