package com.dddev.log.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesUtil {

    @Value("${aes.log.secretKey}")
    private String aesLogSecretKey;

    @Value("${aes.log.iv}")
    private String aesLogIv;

    private final String alg = "AES/CBC/PKCS5Padding";
    
    //암호화
    public String aes256Encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(aesLogSecretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(aesLogIv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    //복호화
    public String aes256Decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(aesLogSecretKey.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(aesLogIv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public Map<String, Integer> getUserInfo(String token) {
        Map<String, Integer> info = new HashMap<>();
        String[] split = token.split("/");
        info.put("groundId", Integer.parseInt(split[0]));
        info.put("userId", Integer.parseInt(split[1]));
        return info;
    }

}
