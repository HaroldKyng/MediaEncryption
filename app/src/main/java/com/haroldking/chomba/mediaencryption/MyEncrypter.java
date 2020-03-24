package com.haroldking.chomba.mediaencryption;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncrypter {

    public final static int READ_WRITE_BLOCK_BUFFER = 1024;
    public final static String ALGO_IMAGE_ENCRYPTOR = "AES/CBC/PKCS5Padding";
    public final static String ALGO_SECRET_KEY = "AES";

    public static void encryptToFile(String keyStr, String spectStr, InputStream in, OutputStream out)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        try {
        IvParameterSpec iv = new IvParameterSpec(spectStr.getBytes("UTF-8"));
        SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);
                Cipher c = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR);

            c.init(Cipher.ENCRYPT_MODE,keySpec,iv);
            out = new CipherOutputStream(out,c);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while((count = in.read(buffer))>0)
            out.write(buffer,0,count);
        } finally {
            out.close();
        }

    }


    public static void decryptToFile(String keyStr, String spectStr, InputStream in, OutputStream out)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IOException {
        try {
            IvParameterSpec iv = new IvParameterSpec(spectStr.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);
            Cipher c = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR);

            c.init(Cipher.DECRYPT_MODE,keySpec,iv);
            out = new CipherOutputStream(out,c);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while((count = in.read(buffer))>0)
            out.write(buffer,0,count);
        } finally {
            out.close();
        }

    }
}
