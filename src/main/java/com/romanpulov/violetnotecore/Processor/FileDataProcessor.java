package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;

public abstract class FileDataProcessor {
    protected byte[] FILE_DATA_SIGNATURE = HexConverter.hexToBytes("AAFG359F");

    abstract protected AESCryptService createCryptService();
}
