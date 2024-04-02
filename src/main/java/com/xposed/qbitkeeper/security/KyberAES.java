package com.xposed.qbitkeeper.security;

import org.bouncycastle.jcajce.SecretKeyWithEncapsulation;
import org.bouncycastle.jcajce.spec.KEMExtractSpec;
import org.bouncycastle.jcajce.spec.KEMGenerateSpec;
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class KyberAES {
    private static final String KEM_ALGORITHM = "Kyber";
    private static final AlgorithmParameterSpec KEM_PARAMETER_SPEC = KyberParameterSpec.kyber512;
    private static final String PROVIDER = "BCPQC";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String MODE_PADDING = "AES/ECB/PKCS5Padding"; // ECB mode with PKCS5 padding

    public static String encrypt(String plainText, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static KeyPair generateKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEM_ALGORITHM, PROVIDER);
        keyPairGenerator.initialize(KEM_PARAMETER_SPEC, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static SecretKeyWithEncapsulation generateSecretKeySender(PublicKey publicKey)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEM_ALGORITHM, PROVIDER);
        KEMGenerateSpec kemGenerateSpec = new KEMGenerateSpec(publicKey, "AES");
        keyGenerator.init(kemGenerateSpec);
        return  (SecretKeyWithEncapsulation)keyGenerator.generateKey();
    }

    public static SecretKeyWithEncapsulation generateSecretKeyReciever(PrivateKey privateKey, byte[] encapsulation)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KEMExtractSpec kemExtractSpec = new KEMExtractSpec(privateKey, encapsulation, "AES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEM_ALGORITHM, PROVIDER);
        keyGenerator.init(kemExtractSpec);
        return (SecretKeyWithEncapsulation)keyGenerator.generateKey();
    }
}
