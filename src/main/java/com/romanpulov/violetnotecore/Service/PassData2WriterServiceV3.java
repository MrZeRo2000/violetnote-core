package com.romanpulov.violetnotecore.Service;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptException;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Processor.FilePassDataWriterV3;

import java.io.IOException;
import java.io.OutputStream;

public class PassData2WriterServiceV3 {
    public static void toStream(OutputStream outputStream, String password, PassData2 passData2)
    throws AESCryptException, IOException, DataReadWriteException
    {
        (new FilePassDataWriterV3(outputStream, password, passData2)).writeFile();
    }
}
