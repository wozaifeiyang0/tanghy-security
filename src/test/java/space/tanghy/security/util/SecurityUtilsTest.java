package space.tanghy.security.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SecurityUtilsTest {

    @Test
    public void privateTest() {
        try {
            // 1.初始化发送方密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            String s = Base64.encodeBase64String(rsaPublicKey.getEncoded());
            String s1 = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
            System.out.println("Public Key:" + s);
            System.out.println("Private Key:" + s1);

            // 2.私钥加密、公钥解密 ---- 加密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(s1));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal("tanghy".getBytes());
            String s2 = Base64.encodeBase64String(result);
            System.out.println("私钥加密、公钥解密 ---- 加密:" + s2);

            // 3.私钥加密、公钥解密 ---- 解密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(s));
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            result = cipher.doFinal(Base64.decodeBase64(s2));
            System.out.println("私钥加密、公钥解密 ---- 解密:" + new String(result));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void publicEncrypt() {

        try{
            // 1.初始化发送方密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            String s = Base64.encodeBase64String(rsaPublicKey.getEncoded());
            String s1 = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
            System.out.println("Public Key:" + s);
            System.out.println("Private Key:" + s1);


            // 4.公钥加密、私钥解密 ---- 加密
            X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(s));
            KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
            PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
            Cipher cipher2 = Cipher.getInstance("RSA");
            cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
            byte[] result2 = cipher2.doFinal("620201-YX-006".getBytes());
            String s3 = Base64.encodeBase64String(result2);
            System.out.println("公钥加密、私钥解密 ---- 加密:" + s3);
            System.out.println("公钥加密、私钥解密 ---- 加密:" + StringUtils.newStringUtf8(result2));

            // 5.私钥解密、公钥加密 ---- 解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(s1));
            KeyFactory keyFactory5 = KeyFactory.getInstance("RSA");
            PrivateKey privateKey5 = keyFactory5.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher5 = Cipher.getInstance("RSA");
            cipher5.init(Cipher.DECRYPT_MODE, privateKey5);
            byte[] result5 = cipher5.doFinal(Base64.decodeBase64(s3));
            System.out.println("公钥加密、私钥解密 ---- 解密:" + new String(result5, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void securityTest() {

        try {
            for (int i = 0; i < 10; i++) {

                Tkey tkey = SecurityUtils.generatorKey();
                String tanghy = SecurityUtils.encrypt("中国", tkey.getPublicKey());
                String decrypt = SecurityUtils.decrypt(tanghy, tkey.getPrivateKey());
                System.out.println(decrypt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 加密的密钥，构造一定长度的字节数组
    private final static byte[] KEY_BYTES = "Vp6flFpGW86g7hi6MhD3Zl2eThJTjPnIjXE4".getBytes();
    private final static int KEY_LENGTH = KEY_BYTES.length;

    @Test
    public void stringTo16() {
        // 编码
        String encode = java.util.Base64.getEncoder().encodeToString(" 123456abcdef".getBytes());

        // 解码
        byte[] decodeByte = java.util.Base64.getDecoder().decode(encode);
        String decode = new String(decodeByte);
        System.out.println(decode);

        byte[] bytes = xorEncode("123456abcdef".getBytes());
        String s = new String(bytes);
        byte[] bytes1 = xorDecode(s.getBytes());
        System.out.println(new String(bytes));

    }

    /**
     * 异或运算加密
     *
     * @param input 要加密的内容
     * @return 加密后的数据
     */
    public static byte[] xorEncode(byte[] input) {
        int keyIndex = 0;
        int length = input.length;
        for (int i = 0; i < length; i++) {
            input[i] = (byte) (input[i] ^ KEY_BYTES[(keyIndex++ % KEY_LENGTH)]);
        }
        return input;
    }

    /**
     * 异或运算解密
     *
     * @param input 要解密的内容
     * @return 解密后的数据
     */
    public static byte[] xorDecode(byte[] input) {
        int keyIndex = 0;
        int length = input.length;
        for (int i = 0; i < length; i++) {
            input[i] = (byte) (input[i] ^ KEY_BYTES[(keyIndex++ % KEY_LENGTH)]);
        }
        return input;
    }

    /**
     * 解密算法
     * @param ciphertext 密文
     * @param secretKey 密钥
     * @return 明文
     */
    public static String decrypt(String ciphertext,String secretKey){
        String plaintext="";
        char[][] chars = new char[2][ciphertext.length()];
        for (int i = 0; i <ciphertext.length() ; i++) {
            chars[0][i]=ciphertext.charAt(i);
            chars[1][i]=secretKey.toUpperCase().charAt(i%(secretKey.length()));
        }
        char[] charArray = ciphertext.toCharArray();
        for (int i = 0; i <charArray.length ; i++) {
            int j = charArray[i];
            if (j>=97&&j<=97+26){
                int k = chars[1][i];
                char te = (char) (((j - 97) + 26-(k - 65)) % 26+65);
                plaintext = plaintext+te;
            }
            if (j>=65&&j<=65+26){
                int k = chars[1][i];
                char te = (char) (((j - 65) + 26-(k - 65)) % 26+97);
                plaintext = plaintext+te;
            }
        }
        return plaintext;
    }


    /**
     * 加密
     * @param plaintext 明文
     * @param secretKey 密钥
     * @return 密文
     */
    public static String encryption(String plaintext,String secretKey){
        String ciphertext = "";
        char[][] chars = new char[2][plaintext.length()];
        for (int i = 0; i <plaintext.length() ; i++) {
            chars[0][i]=plaintext.charAt(i);
            chars[1][i]=secretKey.toUpperCase().charAt(i%(secretKey.length()));
        }
        char[] charArray = plaintext.toCharArray();
        for (int i = 0; i <charArray.length ; i++) {
            int j = charArray[i];
            if (j>=97&&j<=97+26){
                int k = chars[1][i];
                char te = (char) (((j - 97) + (k - 65)) % 26+65);
                ciphertext = ciphertext+te;
            }
            if (j>=65&&j<=65+26){
                int k = chars[1][i];
                char te = (char) (((j - 65) + (k - 65)) % 26+97);
                ciphertext = ciphertext+te;
            }
        }
        return  ciphertext;
    }





}
