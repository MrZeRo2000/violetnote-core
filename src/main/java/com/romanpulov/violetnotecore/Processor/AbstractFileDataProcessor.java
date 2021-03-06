package com.romanpulov.violetnotecore.Processor;

import com.romanpulov.violetnotecore.AESCrypt.AESCryptService;
import com.romanpulov.violetnotecore.Utils.HexConverter;

public abstract class AbstractFileDataProcessor {
    protected byte[] FILE_DATA_SIGNATURE = HexConverter.hexToBytes("AAFG359F");

    protected byte[] header;
    protected byte[] version;

    abstract protected AESCryptService createCryptService();
}
