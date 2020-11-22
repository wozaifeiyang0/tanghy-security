package site.tanghy.security.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

public class SecurityUtils {


    public final static String T_KEY_NAME = "tkey";
    private static Logger logging = LoggerFactory.getLogger(SecurityUtils.class);
    private SecurityUtils() {

    }


    /**
     * 生成秘钥对象方法，默认生成
     * @return 生产秘钥对象
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static Tkey generatorKey() throws NoSuchAlgorithmException, IOException{
        return generatorKey(false);
    }


    /**
     * 生成秘钥对象方法，默认生成
     * @param create
     * @return 生产秘钥对象
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static Tkey generatorKey(boolean create) throws NoSuchAlgorithmException, IOException{
        return generatorKey("", create);
    }


    /**
     * @param keyPath
     * @param create
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static Tkey generatorKey(String keyPath, boolean create) throws NoSuchAlgorithmException, IOException {

        Properties properties = new Properties();

        if (null == keyPath || keyPath.equals("")) {
            keyPath = System.getProperty("user.home");
            keyPath = keyPath + "/.rsa";
        }
        // 秘钥文件是否存在
        File file = new File(keyPath, T_KEY_NAME);
        // 创建秘钥对象
        Tkey tkey = new Tkey();
        if (file.exists() && !create) {

            FileInputStream in = new FileInputStream(file);
            properties.load(in);
            String privateKey = properties.getProperty(TKeyEnum.PRIVATE_KEY.name());
            String publicKey = properties.getProperty(TKeyEnum.PUBLIC_KEY.name());
            in.close();

            tkey.setPublicKey(publicKey);
            tkey.setPrivateKey(privateKey);


        } else {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

            tkey.setPrivateKey(Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
            tkey.setPublicKey(Base64.encodeBase64String(rsaPublicKey.getEncoded()));

            // 判断文件是否存在
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                // 判断文件夹路径是否存在
                if (!parentFile.exists()) {
                    // 创建文件夹路径
                    parentFile.mkdirs();
                }
                // 创建文件
                file.createNewFile();
            }

            ///保存属性到b.properties文件
            FileOutputStream oFile = new FileOutputStream(file, true);//true表示追加打开
            properties.setProperty(TKeyEnum.PUBLIC_KEY.name(), tkey.getPublicKey());
            properties.setProperty(TKeyEnum.PRIVATE_KEY.name(), tkey.getPrivateKey());
            properties.store(oFile, "The New properties file");
            oFile.close();
        }

        logging.debug("Public Key:" + tkey.getPublicKey());
        logging.debug("Private Key:" + tkey.getPrivateKey());

        return tkey;

    }

    public static String encrypt(String content, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher2 = Cipher.getInstance("RSA");
        cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
        byte[] result2 = cipher2.doFinal(content.getBytes());
        String bas64 = Base64.encodeBase64String(result2);
        logging.debug("公钥加密、私钥解密 ---- 加密:" + bas64);
        return bas64;

    }

    public static String decrypt(String content, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory5 = KeyFactory.getInstance("RSA");
        PrivateKey privateKey5 = keyFactory5.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher5 = Cipher.getInstance("RSA");
        cipher5.init(Cipher.DECRYPT_MODE, privateKey5);
        byte[] result5 = cipher5.doFinal(Base64.decodeBase64(content));
        logging.debug("公钥加密、私钥解密 ---- 解密:" + new String(result5));
        return new String(result5);

    }


}
