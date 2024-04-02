package com.xposed.qbitkeeper.security;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class KeyStoreManager {
    public static final String KEYSTORE_FILE = "keystore.jks";
    public static final String KEYSTORE_PASSWORD = "password_to_hai"; //store it in env or app.prop
    public static final String ALIAS_PREFIX = "user_";

    public static void storeSecretKeyForUser(String username, SecretKey secretKey) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] passwordArray = KEYSTORE_PASSWORD.toCharArray();
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            keyStore.load(fis, passwordArray);
        } catch (Exception e) {
            keyStore.load(null, passwordArray);
        }

        String alias = ALIAS_PREFIX + username;
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        keyStore.setEntry(alias, secretKeyEntry, new KeyStore.PasswordProtection(passwordArray));

        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keyStore.store(fos, passwordArray);
        }
    }

    public static SecretKey getSecretKeyForUser(String username) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] passwordArray = KEYSTORE_PASSWORD.toCharArray();
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            keyStore.load(fis, passwordArray);
        }

        String alias = ALIAS_PREFIX + username;
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(passwordArray);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, passwordProtection);

        return secretKeyEntry.getSecretKey();
    }

    public static void deleteSecretKeyForUser(String username) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] passwordArray = KEYSTORE_PASSWORD.toCharArray();
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            keyStore.load(fis, passwordArray);
        }

        String alias = ALIAS_PREFIX + username;
        keyStore.deleteEntry(alias);

        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keyStore.store(fos, passwordArray);
        }
    }
}
