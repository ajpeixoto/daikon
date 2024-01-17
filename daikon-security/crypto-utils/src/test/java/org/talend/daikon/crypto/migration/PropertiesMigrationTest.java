// Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com
package org.talend.daikon.crypto.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySources;

public class PropertiesMigrationTest {

    @Test
    public void shouldReencryptSourceValues() throws Exception {
        // given
        Path tempFile = Files.createTempFile("temp-PropertiesMigrationTest.", ".properties");
        try (InputStream refInStream = getClass().getResourceAsStream("properties_migration.properties")) {
            Files.copy(refInStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        final String plainText = "5ecr3t";
        final Encryption source = new Encryption(KeySources.fixedKey("DataPrepIsSoCool"), CipherSources.aes());
        final Encryption target = new Encryption(KeySources.random(16), CipherSources.getDefault());
        final EncryptionMigration migration = EncryptionMigration.build(source, target);
        final PropertiesMigration propertiesMigration = new PropertiesMigration(migration, //
                tempFile.toAbsolutePath().toString(), //
                Collections.singleton("admin.password") //
        );

        // when
        propertiesMigration.migrate();

        // then
        Properties migratedProperties = new Properties();
        try (FileInputStream fis = new FileInputStream(tempFile.toFile())) {
            migratedProperties.load(fis);
        }
        assertNotEquals(source.encrypt(plainText), migratedProperties.getProperty("admin.password"));
        assertEquals(plainText, target.decrypt(migratedProperties.getProperty("admin.password")));
    }
}