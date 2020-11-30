package space.tanghy.security.util;

import org.junit.Test;

import java.io.IOException;

public class Base64Test {



    @Test
    public void aaaTest() throws IOException {


        String aa = "1-23-12-as/d-af";

        String encryption = CaesarUtils.encrypt(aa);
        System.out.println(encryption);
        String decrypt = CaesarUtils.decrypt(encryption);
        System.out.println(decrypt);


    }

    char[] lowercase1 = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };
    char[] lowercase2 = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    char[] lowercase3 = new char[]{'1','2','3','4','5','6','7','8','9','0'};

    char[] upcase1 = new char[]{'h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','a','b','c','d','e','f','g',
            '1','2','3','4','5','6','7','8','9','0',
            'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G'
    };
    char[] upcase3 = new char[]{'1','2','3','4','5','6','7','8','9','0'};
    char[] upcase2 = new char[]{'H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G'};

    private String encryption(String mw) {


        String result = "";
        for (char m : mw.toCharArray()) {

            String zm = "";
            for (int i = 0; i < lowercase1.length; i++) {

                if (m == lowercase1[i]) {
                    zm += upcase1[i];
                }
            }

            if (zm.equals("")) {
                result += m;
            } else {
                result += zm;
            }

        }

        return result;
    }

    private String decrypt(String mw) {

        String result = "";

        for (char m : mw.toCharArray()) {

            String jm = "";
            for (int i = 0; i < upcase1.length; i++) {

                if (m == upcase1[i]) {
                    jm += lowercase1[i];
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
