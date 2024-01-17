// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.talend.daikon.exception.TalendRuntimeException;

public class PropertiesEncryptionTest {

    private Encryption encryption;

    @BeforeEach
    public void setUp() {
        // Deprecated method is used on purpose here, see PropertiesMigration for encryption migration
        encryption = new Encryption(KeySources.fixedKey("DataPrepIsSoCool"), CipherSources.aes());
    }

    @Test
    public void shouldEncryptAndSaveAllOccurrences() throws Exception {
        // given
        String propertyKey = "admin.password";
        String propertyValue = "5ecr3t";
        String propertyEncodedValue = "JP6lC6hVeu3wRZA1Tzigyg==";
        Path tempFile = Files.createTempFile("dataprep-PropertiesEncryptionTest.", ".properties");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 5; i++) {
                bufferedWriter.write(propertyKey + "=" + propertyValue);
                bufferedWriter.newLine();
            }
        }

        // when
        new PropertiesEncryption(encryption).encryptAndSave(tempFile.toString(), Collections.singleton(propertyKey));

        // then
        final String expectedLine = propertyKey + "=" + propertyEncodedValue;
        verifyContent(expectedLine, tempFile);
    }

    @Test
    public void shouldNotEncryptCommentedProperties() throws Exception {
        // given
        Path tempFile = Files.createTempFile("dataprep-PropertiesEncryptionTest.", ".properties");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            bufferedWriter.write("# file.password = the file password");
            bufferedWriter.newLine();
            bufferedWriter.write("file = /tmp");
            bufferedWriter.newLine();
        }

        // when
        new PropertiesEncryption(encryption).encryptAndSave(tempFile.toString(), Collections.singleton("file.password"));

        // then
        try (BufferedReader reader = Files.newBufferedReader(tempFile)) {
            String line = reader.readLine();
            assertEquals("# file.password = the file password", line);
            line = reader.readLine();
            assertEquals("file = /tmp", line);
        }
    }

    @Test
    public void decryptAndSave() throws Exception {
        // given
        String propertyKey = "admin.password";
        String propertyEncodedValue = "JP6lC6hVeu3wRZA1Tzigyg==";
        String propertyValue = "5ecr3t";
        Path tempFile = Files.createTempFile("dataprep-PropertiesEncryptionTest.", ".properties");
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 5; i++) {
                bufferedWriter.write(propertyKey + "=" + propertyEncodedValue);
                bufferedWriter.newLine();
            }
        }

        // when
        new PropertiesEncryption(encryption).decryptAndSave(tempFile.toString(), Collections.singleton(propertyKey));

        // then
        final String expectedLine = propertyKey + "=" + propertyValue;
        verifyContent(expectedLine, tempFile);
    }

    @Test
    public void encryptAndSave_doesNotBreakLayout() throws Exception {
        Path tempFile = Files.createTempFile("temp-PropertiesEncryptionTest.", ".properties");
        try (InputStream refInStream = getClass().getResourceAsStream("keep-layout-test-in.properties")) {
            Files.copy(refInStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        new PropertiesEncryption(encryption).encryptAndSave(tempFile.toString(), Collections.singleton("admin.password"));

        String tempContent = FileUtils.readFileToString(tempFile.toFile(), StandardCharsets.UTF_8);
        if (SystemUtils.IS_OS_WINDOWS) {
            tempContent = tempContent.replaceAll("\r\n", "\n");
        }

        assertEquals(
                FileUtils.readFileToString(new File(getClass().getResource("keep-layout-test-expected.properties").getFile()),
                        StandardCharsets.UTF_8),
                tempContent);
    }

    @Test
    public void encryptAndSave_doesNotAddExtraProperties() throws Exception {
        Path tempFile = Files.createTempFile("temp-PropertiesEncryptionTest.", ".properties");
        try (InputStream refInStream = getClass().getResourceAsStream("keep-layout-test-in.properties")) {
            Files.copy(refInStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        new PropertiesEncryption(encryption).encryptAndSave(tempFile.toString(),
                new HashSet<>(Arrays.asList("admin.password", "unknown")));

        String tempContent = FileUtils.readFileToString(tempFile.toFile(), StandardCharsets.UTF_8);
        if (SystemUtils.IS_OS_WINDOWS) {
            tempContent = tempContent.replaceAll("\r\n", "\n");
        }

        assertEquals(
                FileUtils.readFileToString(new File(getClass().getResource("keep-layout-test-expected.properties").getFile())),
                tempContent);
    }

    @Test
    public void loadAndDecrypt_fileNotFound() {
        assertThrows(TalendRuntimeException.class, () -> {
            new PropertiesEncryption(encryption).loadAndDecrypt("NOT_FOUND", Collections.singleton("admin.password"));
        });
    }

    @Test
    public void loadAndDecrypt_clear() throws Exception {
        URL resource = getClass().getResource("keep-layout-test-in.properties");
        String input = Paths.get(resource.toURI()).toString();
        Properties result = new PropertiesEncryption(encryption).loadAndDecrypt(input, Collections.singleton("admin.password"));
        assertEquals("5ecr3t", result.getProperty("admin.password"));
        assertEquals("http://fr.talend.com/", result.getProperty("url"));
    }

    @Test
    public void loadAndDecrypt_encrypted() throws Exception {
        URL resource = getClass().getResource("keep-layout-test-expected.properties");
        String input = Paths.get(resource.toURI()).toString();
        Properties result = new PropertiesEncryption(encryption).loadAndDecrypt(input, Collections.singleton("admin.password"));
        assertEquals("5ecr3t", result.getProperty("admin.password"));
        assertEquals("http://fr.talend.com/", result.getProperty("url"));
    }

    private void verifyContent(String expectedLine, Path tempFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(tempFile)) {
            String line = reader.readLine();
            while (line != null) {
                assertEquals(expectedLine, line);
                line = reader.readLine();
            }
        }
    }
}