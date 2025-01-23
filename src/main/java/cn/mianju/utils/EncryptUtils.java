package cn.mianju.utils;

import cn.mianju.entity.EncryptInfo;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class EncryptUtils {

    private static Cipher cipher;

    /**
     * 生成RSA密钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static EncryptInfo generateRsaKey() throws NoSuchAlgorithmException {


        // 创建RSA密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，指定密钥长度为2048位
        keyPairGenerator.initialize(2048);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取公钥
        PublicKey publicKey = keyPair.getPublic();
        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        EncryptInfo encryptInfo = new EncryptInfo();
        encryptInfo.setPublicKey(publicKeyString);
        encryptInfo.setPrivateKey(privateKeyString);

        return encryptInfo;
    }


    public static String encryptRsa(String data, String publicKey) throws Exception {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyFromString(publicKey));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decryptRsa(String data, String privateKey) throws Exception {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKeyFromString(privateKey));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data));
        return new String(decryptedBytes);
    }

    public static String sha256Encode(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static PublicKey getPublicKeyFromString(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 移除公钥字符串中的前缀和后缀（如果有）
        publicKeyStr = publicKeyStr.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // 解码Base64编码的公钥字符串
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);

        // 使用X509EncodedKeySpec来生成PublicKey对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey getPrivateKeyFromString(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        privateKeyStr = privateKeyStr.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
