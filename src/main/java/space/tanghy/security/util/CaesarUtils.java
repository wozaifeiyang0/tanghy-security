package space.tanghy.security.util;


/**
 * author tanghy
 * time 2020-12-03
 * <p>凯撒加密，解密工具，内置明文字典表和密文字典表</p>
 */
public class CaesarUtils {


    // 明文字典表
    private final static char[] caesarDictionary = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    };
    // 密文字典表
    private final static char[] caesarComparison = new char[]{'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G'
    };


    /**
     * 加密方法
     *
     * @param mw     需要加密的字符串
     * @param offset 偏移量
     * @return 返回加密的字符串
     */
    public static String encrypt(String mw, int offset) {
        return handler(mw, offset, caesarDictionary, caesarComparison, false);
    }

    /**
     * 解密方法
     *
     * @param mw     加密后的字符串
     * @param offset 偏移量
     * @return 返回解密后的字符串
     */
    public static String decrypt(String mw, int offset) {
        return handler(mw, offset, caesarComparison, caesarDictionary, true);
    }

    /**
     * 加密方法
     *
     * @param mw 需要加密的字符串
     * @return 返回加密的字符串
     */
    public static String encrypt(String mw) {
        return handler(mw, 0, caesarDictionary, caesarComparison, false);
    }

    /**
     * 解密方法
     *
     * @param mw 加密后的字符串
     * @return 返回解密后的字符串
     */
    public static String decrypt(String mw) {
        return handler(mw, 0, caesarComparison, caesarDictionary, true);
    }

    /**
     * 通用处理方法
     *
     * @param mw               加密或解密的字符串
     * @param offset           偏移量，数值要大于0
     * @param caesarDictionary 明文字符串对应表
     * @param caesarComparison 密文字符串对应表
     * @param isDecrypt        为true时加密，false时解密
     * @return 返回加密或者解密后的字符串
     */
    private static String handler(String mw, int offset, char[] caesarDictionary, char[] caesarComparison, boolean isDecrypt) {


        StringBuilder result = new StringBuilder();
        for (char m : mw.toCharArray()) {
            StringBuilder jm = new StringBuilder();
            for (int i = 0; i < caesarComparison.length; i++) {

                if (m == caesarComparison[i]) {
                    int offsetIndex = offset % caesarComparison.length;

                    if (!isDecrypt) {
                        if (offsetIndex + i >= caesarComparison.length) {
                            jm.append(caesarDictionary[offsetIndex + i - caesarComparison.length]);
                        } else if (offsetIndex + i < 0) {
                            jm.append(caesarDictionary[i + offsetIndex + caesarComparison.length]);
                        } else {
                            jm.append(caesarDictionary[i + offsetIndex]);
                        }
                    } else {

                        if (i - offsetIndex < 0) {
                            jm.append(caesarDictionary[i - offsetIndex + caesarComparison.length]);
                        } else if (i - offsetIndex >= caesarComparison.length) {
                            jm.append(caesarDictionary[i - offsetIndex - caesarComparison.length]);
                        } else {
                            jm.append(caesarDictionary[i - offsetIndex]);
                        }
                    }
                }
            }
            if (jm.toString().equals("")) {
                result.append(m);
            } else {
                result.append(jm);
            }

        }
        return result.toString();

    }
}
