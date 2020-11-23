package space.tanghy.security.util;


import org.apache.commons.codec.binary.Base64;
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
            byte[] result2 = cipher2.doFinal("tanghy".getBytes());
            String s3 = Base64.encodeBase64String(result2);
            System.out.println("公钥加密、私钥解密 ---- 加密:" + s3);

            // 5.私钥解密、公钥加密 ---- 解密
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(s1));
            KeyFactory keyFactory5 = KeyFactory.getInstance("RSA");
            PrivateKey privateKey5 = keyFactory5.generatePrivate(pkcs8EncodedKeySpec5);
            Cipher cipher5 = Cipher.getInstance("RSA");
            cipher5.init(Cipher.DECRYPT_MODE, privateKey5);
            byte[] result5 = cipher5.doFinal(Base64.decodeBase64(s3));
            System.out.println("公钥加密、私钥解密 ---- 解密:" + new String(result5));

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



}
