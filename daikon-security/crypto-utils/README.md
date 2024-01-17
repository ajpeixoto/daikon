# Talend Daikon - Crypto utils

## Description

This module contains helper for encryption and decryption of data.

## Build

To build module, simply run `mvn install`

```bash
$> mvn install
```

## Usage

This library offers utilities for both password digest and encryption.

## Password Digest

It is not permitted to use a SHA digest to digest passwords, instead they must be digested using
one of the approved implementations detailed in this section. Argon2Id should be the preferred solution for password digests.

### Argon2Id

```java
public static void main(String[] args) throws Exception {
    PasswordDigester digester = new Argon2PasswordDigester();
    
    final String digest = digester.digest("tiger");
    System.out.println("digest = " + digest);
    
    System.out.println("Validate 'tiger': " + digester.validate("tiger", digest));
    System.out.println("Validate 'password': " + digester.validate("password", digest));
}
```

The code above produces this example:
```text
digest = $argon2id$v=19$m=4096,t=3,p=1$LqFyRbm7jnNpUEjlpapM6A$yqkHrp8eEJkCvM2lkAxraJtFN2vt2LhSynpKJosPgE8
Validate 'tiger': true
Validate 'password': false
```
The Argon2PasswordDigester uses spring-security-crypto as the underlying implementation, which is an optional jar
in crypto-utils, so to use this algorithm this jar will need to be added to dependency control. In addition, the BouncyCastle
bcprov-jdk15on jar is also required to be added.

According to the
[OWASP password storage cheatsheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html):

> Use Argon2id with a minimum configuration of 15 MiB of memory, an iteration count of 2, and 1 degree of parallelism.

By default, the Argon2PasswordDigester uses the following values, which should be considered to be secure:

 * Salt Length: 16
 * Hash Length: 32
 * Iterations: 3
 * Parallelism: 1
 * Memory: 4096 (Kb)

### BCrypt

```java
public static void main(String[] args) throws Exception {
    PasswordDigester digester = new BCryptPasswordDigester();
    
    final String digest = digester.digest("tiger");
    System.out.println("digest = " + digest);
    
    System.out.println("Validate 'tiger': " + digester.validate("tiger", digest));
    System.out.println("Validate 'password': " + digester.validate("password", digest));
}
```

The code above produces this example:
```text
digest = $2a$10$f0vioWEm1hFi6gUnxTUK.u4GssfYd6KsdUsBTDVZ8RdXw6gU85Hvi
Validate 'tiger': true
Validate 'password': false
```

The BCryptPasswordDigester uses spring-security-crypto as the underlying implementation, which is an optional jar
in crypto-utils, so to use this algorithm this jar will need to be added to dependency control.

According to the
[OWASP password storage cheatsheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html),
a minimum strength of 10 should be used with BCrypt (this is the default). You should not need to configure the
BCrypt algorithm, but instead just use the default version.

Please note that BCrypt does not work with passwords greater than 72 bytes, one of the other implementations
should be chosen instead for this requirement.

### PBKDF2

```java
public static void main(String[] args) throws Exception {
    PasswordDigester digester = new PBKDF2PasswordDigester();
    
    final String digest = digester.digest("tiger");
    System.out.println("digest = " + digest);
    
    System.out.println("Validate 'tiger': " + digester.validate("tiger", digest));
    System.out.println("Validate 'password': " + digester.validate("password", digest));
}
```

The code above produces this example:
```text
digest = rGl+Xu8NbNqqJdmbTk6uHg==-YUdLMtZL+3renFPkDt6SSoDKobFhmijkOmpXuvhjapo=
Validate 'tiger': true
Validate 'password': false
```
According to the
[OWASP password storage cheatsheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html):

> If FIPS-140 compliance is required, use PBKDF2 with a work factor of 310,000 or more and set with an internal hash function of HMAC-SHA-256.

The PBKDF2PasswordDigester uses a random 16 byte salt by default, with a 256 bit key length and 310,000 iteration count.

## Encryption

The main entry point for this module is the class `Encryption`. It needs as input:

* A `KeySource`: the key that will be used to initialize the encryption algorithm. This is a critical piece of configuration: it cannot be a constant but it must be a value application must be able to find again (in case of an application restart, for example).
* A `CipherSource`: the type of algorithm you want to use for encryption and decryption.

Both `KeySource` and `CipherSource` have helpers to ease integration in your code. A quick example looks like this:

```java
public static void main(String[] args) throws Exception {
    final KeySource keySource = KeySources.machineUID(16); // (1)
    final CipherSource cipherSource = CipherSources.getDefault(); // (2) 
    final Encryption encryption = new Encryption(keySource, cipherSource);

    final String encrypt1 = encryption.encrypt("MyUnsecureString");
    final String encrypt2 = encryption.encrypt("MyUnsecureString");

    System.out.println("encrypt (1) = " + encrypt1);
    System.out.println("encrypt (2) = " + encrypt2);

    System.out.println("decrypt (1) = " + encryption.decrypt(encrypt1));
    System.out.println("decrypt (2) = " + encryption.decrypt(encrypt2));
}
```

A quick explanation: 
* (1) a generated key of 16 bytes will be created using the MAC addresses of the network interfaces on the machine that runs the code.
* (2) a AES/GCM encryption algorithm with a random IV of 16 bytes. It means each value will be encrypted using an additional random value (in addition of the key generated by (1)).

Running this example may create the following output:

```text
encrypt (1) = LPX1zmpCZFyS3a2rDkqnJr+nM3tTOav4kABKBhvTT9alPbAxuPrOywLP5eEDFnin
encrypt (2) = +0t6DcPTYfbZLHNQWF6z6cv3QtUH9EBDAlVyAtGrOokUHl8cZMJ1SNaCa8dNZQtp
decrypt (1) = MyUnsecureString
decrypt (2) = MyUnsecureString
```

### Available CipherSource

The `CipherSources::aes()` gives a standard AES encryption support (with *random* value). Encryption of the same value will always produce the same result.
The usage of `CipherSources::getDefault()` is strongly recommended. `CipherSources::getDefault()` is equivalent to calling `CipherSources::aesGcm(12, 16, null)`, which uses AES in GCM mode (which gives you authenticated encryption) using a 12 byte IV and a 16 byte authentication tag. The last parameter refers to a JCE
Provider that you can optionally use, e.g. to use BouncyCastle pass 'new BouncyCastleProvider()' for this parameter. However this should be unnecessary for
most use-cases.

### Available KeySource

* `KeySources::machineUID`: generate key out of network interfaces MAC address.
* `KeySources::systemProperty`: retrieve key value from a system property.
* `KeySources::fixedKey`: use provided value as key. This one is only here to ease migration to a more secure key generation and use of it is discouraged.

## Migrations

If you want change the encryption of previously encrypted values, you may consider `EncryptionMigration`. To perform migration, you need:

* The source encryption (`KeySource` and `CipherSource`)
* The target encryption (`KeySource` and `CipherSource`)

```java
Encryption source = new Encryption(KeySources.fixedKey("DataPrepIsSoCool"), CipherSources.aes());
Encryption target = new Encryption(KeySources.random(16), CipherSources.getDefault());
EncryptionMigration migration = EncryptionMigration.build(source, target);
String originalEncrypted = "JP6lC6hVeu3wRZA1Tzigyg==";

String migrated = migration.migrate(originalEncrypted);
System.out.println("Original & encrypted: " + originalEncrypted);
System.out.println("Migrated & encrypted: " + migrated);
```

### Properties file migrations

Similarly to value migration, the class `PropertiesMigration` provides tool to migrate a property file:

```java
Encryption source = ... // omitted
Encryption target = ... // omitted
EncryptionMigration migration = ... // omitted (same as previous example)
PropertiesMigration propertiesMigration = new PropertiesMigration(migration, //
    "/usr/local/application/config.properties", //
    Collections.singleton("admin.password") //
);
```

The code above reads and modifies the file `config.properties`: the decrypted value of `admin.password` is encrypted using 
target encryption. 

## Support

You can ask for help on our [forum](https://community.talend.com/).


## Contributing

We welcome contributions of all kinds from anyone.

Using the [Talend bugtracker](https://jira.talendforge.org/projects/TDKN) is the best channel for bug reports and feature requests. Use [GitHub](https://github.com/Talend/daikon) to submit pull requests.

For code formatting, please use the configuration file and setup for Eclipse or IntelliJ that you find here: https://github.com/Talend/tools/tree/master/tools-java-formatter


## License

Copyright 2005 - 2024 Talend, Inc., All Rights Reserved - www.talend.com

Licensed under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt)
