package space.tanghy.security.util;

public class CaesarUtils {


    // 明文字典表
    private final static char[] caesarDictionary = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };
    // 密文字典表
    private final static char[] caesarComparison = new char[]{'h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','a','b','c','d','e','f','g',
            '1','2','3','4','5','6','7','8','9','0',
            'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G'
    };


    /**
     * 加密方法
     * @param mw 需要加密的字符串
     * @return 返回加密的字符串
     */
    public static String encrypt(String mw, int offset) throws Exception {
        String result = handler(mw, offset,caesarDictionary, caesarComparison, false);
        return result;
    }

    /**
     * 解密方法
     * @param mw 加密后的字符串
     * @return 返回解密后的字符串
     */
    public static String decrypt(String mw, int offset) throws Exception {
        String result = handler(mw,offset,caesarComparison,caesarDictionary, true);
        return result;
    }

    /**
     * 加密方法
     * @param mw 需要加密的字符串
     * @return 返回加密的字符串
     */
    public static String encrypt(String mw) throws Exception {
        String result = handler(mw, 0,caesarDictionary, caesarComparison, false);
        return result;
    }

    /**
     * 解密方法
     * @param mw 加密后的字符串
     * @return 返回解密后的字符串
     */
    public static String decrypt(String mw) throws Exception {
        String result = handler(mw,0,caesarComparison,caesarDictionary, true);
        return result;
    }

    /**
     *  通用处理方法
     * @param mw 加密或解密的字符串
     * @param offset 偏移量，数值要大于0
     * @param caesarDictionary 明文字符串对应表
     * @param caesarComparison 密文字符串对应表
     * @param isDecrypt 为true时加密，false时解密
     * @return 返回加密或者解密后的字符串
     */
    private static String handler(String mw, int offset, char[] caesarDictionary, char[] caesarComparison, boolean isDecrypt) throws Exception {

        if (offset < 0) {
            throw new Exception("偏移量只能为正数");
        }

        String result = "";
        for (char m : mw.toCharArray()) {
            String jm = "";
            for (int i = 0; i < caesarComparison.length; i++) {

                if (m == caesarComparison[i]) {
                    int offsetIndex = offset % caesarComparison.length;

                    if (!isDecrypt) {
                        if (offsetIndex + i >= caesarComparison.length) {
                            jm += caesarDictionary[offsetIndex + i - caesarComparison.length];
                        } else {
                            jm += caesarDictionary[i + offsetIndex];
                        }
                    } else {

                        if (i - offsetIndex < 0) {
                            jm += caesarDictionary[i - offsetIndex + caesarComparison.length];
                        } else {
                            jm += caesarDictionary[i - offsetIndex];
                        }

                    }

                }
            }
            if (jm.equals("")) {
                result += m;
            } else {
                result += jm;
            }

        }
        return result;

    }
}
