package space.tanghy.security.util;

/**
 *  秘钥对象，包含两个属性，公钥字符串和私钥字符串
 */
public class Tkey {

    // 公钥字符串
    private String publicKey;
    // 私钥字符串
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
