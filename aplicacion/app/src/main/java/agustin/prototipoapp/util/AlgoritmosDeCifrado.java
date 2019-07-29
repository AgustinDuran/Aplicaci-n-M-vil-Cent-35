package agustin.prototipoapp.util;

/**
 * Md5:
 * https://www.youtube.com/watch?v=fIVYV6KYJqM
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AlgoritmosDeCifrado {

    public static byte[] getCifradoMD5(byte[] cadena) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(cadena);
        return md5.digest();
    }

}
