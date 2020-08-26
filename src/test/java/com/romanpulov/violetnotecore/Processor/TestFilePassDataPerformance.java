package com.romanpulov.violetnotecore.Processor;


import com.romanpulov.violetnotecore.Converter.PassData2Converter;
import com.romanpulov.violetnotecore.Model.PassData;
import com.romanpulov.violetnotecore.Model.PassData2;
import com.romanpulov.violetnotecore.Processor.Exception.DataReadWriteException;
import com.romanpulov.violetnotecore.Service.PassData2ReaderServiceV2;
import com.romanpulov.violetnotecore.Service.PassData2WriterServiceV2;
import com.romanpulov.violetnotecore.TestFileManagement;
import com.romanpulov.violetnotecore.TestPassData2Generator;
import com.romanpulov.violetnotecore.TestPassDataTools;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFilePassDataPerformance {
    private static final String TEST_FILE_NAME_1 = "data\\test_performance_out_1.vnf";
    private static final String TEST_FILE_NAME_2 = "data\\test_performance_out_2.vnf";
    private static final String TEST_PASSWORD = "123456";
    private static final int NUM_CATEGORIES = 30;
    private static final int NUM_NOTES = 120;

    PassData passData = TestPassDataTools.generateTestPassData(NUM_CATEGORIES, NUM_NOTES);
    PassData2 passData2 = TestPassData2Generator.generateTestPassData2(NUM_CATEGORIES, NUM_NOTES);

    private void printExecutionTime(String caption, long startTime, long endTime) {
        long durationNS = endTime - startTime;
        long durationMS = durationNS / 1000000;
        System.out.println(String.format("Execution time of %s is %d ms (%d.ns)", caption, durationMS, durationNS));
    }

    @Test
    @Order(1)
    public void testWriteFile1() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME_1)).testDeleteOutputFile();

        try (OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME_1))) {

            long startTime = System.nanoTime();

            FilePassDataWriterV1 writerV1 = new FilePassDataWriterV1(outputStream, TEST_PASSWORD, passData);
            (new TestFilePassDataWriter(writerV1, TEST_FILE_NAME_1)).testWriteFile();

            long endTime = System.nanoTime();

            printExecutionTime("Write file 1", startTime, endTime);
        }
    }

    @Test
    @Order(2)
    public void testReadFile1() throws Exception {
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1))
        {
            long startTime = System.nanoTime();

            FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, TEST_PASSWORD);
            readerV1.readFile();

            long endTime = System.nanoTime();
            printExecutionTime("Read file 1", startTime, endTime);
        }
    }

    @Test
    @Order(3)
    public void testWriteFile2() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME_2)).testDeleteOutputFile();

        try (OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME_2)))
        {
            long startTime = System.nanoTime();

            FilePassDataWriterV2 writerV2 = new FilePassDataWriterV2(outputStream, TEST_PASSWORD, passData2);
            (new TestFilePassDataWriter(writerV2, TEST_FILE_NAME_2)).testWriteFile();

            long endTime = System.nanoTime();

            printExecutionTime("Write file 2", startTime, endTime);
        }
    }

    @Test
    @Order(4)
    public void testWriteServiceFile2() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME_2)).testDeleteOutputFile();

        try (OutputStream outputStream = new FileOutputStream(new File(TEST_FILE_NAME_2)))
        {
            long startTime = System.nanoTime();

            PassData2WriterServiceV2.toStream(outputStream, TEST_PASSWORD, passData2);

            long endTime = System.nanoTime();

            printExecutionTime("Write file service file 2", startTime, endTime);
        }
    }

    @Test
    @Order(5)
    public void testReadFile2() throws Exception {
        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_2))
        {
            long startTime = System.nanoTime();

            FilePassDataReaderV2 readerV2 = new FilePassDataReaderV2(inputStream, TEST_PASSWORD);
            PassData2 readPassData2 = readerV2.readFile();

            long endTime = System.nanoTime();
            printExecutionTime("Read file 2", startTime, endTime);

            startTime = System.nanoTime();

            String passDataEquals = TestPassDataTools.passDataEquals(passData2, readPassData2);
            if (passDataEquals != null) {
                fail(passDataEquals);
            }

            endTime = System.nanoTime();
            printExecutionTime("PassData2 equality check", startTime, endTime);
        }
    }

    @Test
    @Order(6)
    public void testReadWrongFile() throws Exception {
        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1))
                {
                    FilePassDataReaderV2 readerV2 = new FilePassDataReaderV2(inputStream, TEST_PASSWORD);
                    readerV2.readFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });

        assertThrows(DataReadWriteException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_2))
                {
                    FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, TEST_PASSWORD);
                    readerV1.readFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        });
    }

    @Test
    @Order(7)
    public void testReadOldVersionFile() throws Exception {

        PassData2 passData2 = null;

        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1)) {
            FilePassDataReaderV2 readerV2 = new FilePassDataReaderV2(inputStream, TEST_PASSWORD);
            passData2 = readerV2.readFile();
            fail("Should raise exception after reading the wrong file");
        } catch (DataReadWriteException e) {
            try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1)) {
                FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(inputStream, TEST_PASSWORD);
                PassData passData = readerV1.readFile();
                passData2 = PassData2Converter.from(passData);
            }
        }

        assertNotNull(passData2);
    }

    @Test
    @Order(8)
    public void testReadMarkOldVersionFile() throws Exception {

        PassData2 passData2;

        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1)) {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                bufferedInputStream.mark(10);

                try {
                    FilePassDataReaderV2 readerV2 = new FilePassDataReaderV2(bufferedInputStream, TEST_PASSWORD);
                    readerV2.readFile();
                    fail("Should raise exception after reading the wrong file");
                } catch (DataReadWriteException e) {
                    bufferedInputStream.reset();

                    FilePassDataReaderV1 readerV1 = new FilePassDataReaderV1(bufferedInputStream, TEST_PASSWORD);
                    PassData passData = readerV1.readFile();
                    passData2 = PassData2Converter.from(passData);

                    assertNotNull(passData2);
                }
            }
        }
    }

    @Test
    @Order(9)
    public void testReadServiceOldVersionFile() throws Exception {

        try (InputStream inputStream = new FileInputStream(TEST_FILE_NAME_1)) {
            PassData2 passData2 = PassData2ReaderServiceV2.fromStream(inputStream, TEST_PASSWORD);

            assertNotNull(passData2);
            assertEquals(NUM_CATEGORIES, passData2.getCategoryList().size());
            assertEquals(NUM_NOTES, passData2.getCategoryList().get(0).getNoteList().size());
        }
    }


    @Test
    @Order(10)
    public void testDeleteOutputFile() throws Exception {
        (new TestFileManagement(TEST_FILE_NAME_1)).testDeleteExistingFile();
        (new TestFileManagement(TEST_FILE_NAME_2)).testDeleteExistingFile();
    }
}
