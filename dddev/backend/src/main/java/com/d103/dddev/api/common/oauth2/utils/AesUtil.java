package com.d103.dddev.api.common.oauth2.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AesUtil {

	@Value("${aes.pat.secretKey}")
	private String aesPatSecretKey;

	@Value("${aes.pat.iv}")
	private String aesPatIv;

	@Value("${aes.log.secretKey}")
	private String aesLogSecretKey;

	@Value("${aes.log.iv}")
	private String aesLogIv;

	private final String alg = "AES/CBC/PKCS5Padding";

	public String aes256Encrypt(String text, AesType aesType) throws Exception{
		if(aesType == AesType.PAT) {
			return aes256Encrypt(text, aesPatSecretKey, aesPatIv);
		} else if(aesType == AesType.LOG) {
			return aes256Encrypt(text, aesLogSecretKey, aesLogIv);
		} else {
			return null;
		}
	}

	public String aes256Decrypt(String cipherText, AesType aesType) throws Exception {
		if(aesType == AesType.PAT) {
			return aes256Decrypt(cipherText, aesPatSecretKey, aesPatIv);
		} else if(aesType == AesType.LOG) {
			return aes256Decrypt(cipherText, aesLogSecretKey, aesLogIv);
		} else {
			return null;
		}
	}

	public String aes256Encrypt(String text, String secretKey, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String aes256Decrypt(String cipherText, String secretKey, String iv) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
		byte[] decrypted = cipher.doFinal(decodedBytes);

		return new String(decrypted, StandardCharsets.UTF_8);
	}
}
