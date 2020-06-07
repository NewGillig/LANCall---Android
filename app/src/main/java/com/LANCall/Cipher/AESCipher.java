package com.LANCall.Cipher;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AESCipher {

    SecretKeySpec key;
    Cipher encryptCipher;
    Cipher decryptCipher;
    byte[] password = null;
    byte[] iv = null;
    IvParameterSpec ivSpec = null;


    private byte[] hex2bytes(String hex) {
        int m = 0, n = 0;
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte) intVal);
        }
        return ret;
    }

    private String bytes2hex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
        }
        return sb.toString().trim();
    }

    public AESCipher(String password) {
        this.password = hex2bytes(password);
        this.key = new SecretKeySpec(this.password, "AES");
        this.iv = new byte[] { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
        this.ivSpec = new IvParameterSpec(iv);
        try {
            this.encryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
            this.encryptCipher.init(Cipher.ENCRYPT_MODE,this.key,ivSpec);
            this.decryptCipher = Cipher.getInstance("AES/CFB/NoPadding");
            this.decryptCipher.init(Cipher.DECRYPT_MODE,this.key,ivSpec);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(byte[] content)
    {
        try {
            return this.encryptCipher.doFinal(content);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] decrypt(byte[] content)
    {
        try {
            return this.decryptCipher.doFinal(content);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }



}
