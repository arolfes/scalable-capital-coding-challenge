package com.github.arolfes.scalable.capital.utils;

public final class StringUtils {

    private StringUtils() {
        // empty default constructor
    }

    public static String extractFileName(String address) {
        int to = address.length();
        if (address.contains("&")) {
            to = address.indexOf("&");
        }
        return address.substring(address.indexOf("//") + 2, to).replaceAll("/", "_") + ".html";
    }

    public static String removeUselessUrlChars(String s) {
        String urlToDownload = removeAllAfterFirstQuestionMark(s);
        urlToDownload = removeAllAfterFirstEncodedQuestionMark(urlToDownload);
        urlToDownload = removeAllAfterFirstAmpersand(urlToDownload);
        return removeLastSlash(urlToDownload);
    }

    public static String removeUselessJsChars(String s) {
        String jsUrl = removeAllAfterFirstQuestionMark(s);
        jsUrl = removeAllAfterFirstEncodedQuestionMark(jsUrl);
        jsUrl = removeAllAfterFirstAmpersand(jsUrl);
        if (jsUrl.endsWith("/api/js")) {
            jsUrl = specialGoogleApiHandling(jsUrl);
        } else {
            jsUrl = removeAllBeforeLastSlash(jsUrl);
        }
        return jsUrl;
    }

    // visible for testing purpose
    // @VisibleForTesting
    protected static String specialGoogleApiHandling(String s) {
        String t1 = s.substring(s.lastIndexOf("/api/js"));
        String t3 = s.substring(0, s.lastIndexOf("/api/js"));
        String t2 = t3.substring(t3.lastIndexOf("/") + 1, t3.length());
        return t2 + t1;
    }

    // visible for testing purpose
    protected static String removeAllAfterFirstAmpersand(String s) {
        if (s.contains("&")) {
            return s.substring(0, s.indexOf("&"));
        }
        return s;
    }

    // visible for testing purpose
    protected static String removeAllAfterFirstEncodedQuestionMark(String s) {
        if (s.contains("%3F")) {
            return s.substring(0, s.indexOf("%3F"));
        }
        return s;
    }

    // visible for testing purpose
    protected static String removeAllAfterFirstQuestionMark(String s) {
        if (s.contains("?")) {
            return s.substring(0, s.indexOf("?"));
        }
        return s;
    }

    // visible for testing purpose
    protected static String removeLastSlash(String s) {
        if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    // visible for testing purpose
    protected static String removeAllBeforeLastSlash(String s) {
        if (s.contains("/")) {
            return s.substring(s.lastIndexOf("/") + 1);
        }
        return s;
    }


}
