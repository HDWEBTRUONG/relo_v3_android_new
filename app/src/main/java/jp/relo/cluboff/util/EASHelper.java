package jp.relo.cluboff.util;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tonkhanh on 6/16/17.
 */

public class EASHelper {
    public static String password="cluboffapp0244az";
    public static String padding="pkcs5padding";
}
