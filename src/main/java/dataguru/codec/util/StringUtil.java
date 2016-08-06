package dataguru.codec.util;

import java.util.Vector;

/**
 * StringUtil contains utility functions for manipulating strings.
 *
 */
public class StringUtil {

    /**
     * Creates a new instance of StringUtil
     */
    private StringUtil() {
    }

    /**
     * Split a String and put the results into a vector
     *
     * @param original
     * @param separator
     * @return
     */
    static public Vector splitToVector(String original, char separator) {
        String[] tmp = split(original, separator);
        Vector vtmp = new Vector();
        if (tmp.length > 0) {
            for (int i = 0; i < tmp.length; i++) {
                vtmp.addElement(tmp[i]);
            }
        } else {
            return null;
        }
        return vtmp;
    }

    /**
     * Split a String into multiple strings
     *
     * @param original  Original string
     * @param separator Separator char in original string
     * @return String array containing separated substrings
     */
    static public String[] split(String original, char separator) {
        return split(original, String.valueOf(separator));
    }

    /**
     * Split string into multiple strings
     *
     * @param original  Original string
     * @param separator Separator string in original string
     * @return Splitted string array
     */
    static public String[] split(String original, String separator) {
        Vector nodes = new Vector();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
            }
        }
        return result;
    }

    /**
     * Split a Set of NMEA Strings and put the results into a vector
     *
     * @param original
     * @return
     */
    synchronized static public Vector splitToNMEAVector(String original) {

        Vector nodes = new Vector();
        String separator = "$";

        // Parse nodes into vector
        int index = original.indexOf(separator);
        original = original.substring(index + separator.length());
        index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(separator + original.substring(0, index));

            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(separator + original);

        return nodes;
    }

    /**
     * Reverse given String
     */
    public static String reverse(String text) {
        return new StringBuffer(text).reverse().toString();
    }

    public static String integerToString(int i) {
        String str1 = Integer.toString(i);
        if (i < 10 && i >= 0) {
            str1 = "0" + str1;
        }
        return str1;
    }

    /**
     * Parse string to short, return defaultValue is parse fails
     */
    public static short parseShort(String value, short defaultValue) {
        short parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Short.parseShort(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }

    /**
     * Parse string to int, return defaultValue is parse fails
     */
    public static int parseInteger(String value, int defaultValue) {
        int parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }

    /**
     * Parse string to double, return defaultValue is parse fails
     */
    public static double parseDouble(String value, double defaultValue) {
        double parsed = defaultValue;
        if (value != null) {
            try {
                parsed = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                parsed = defaultValue;
            }
        }
        return parsed;
    }

    /* Replace all instances of a String in a String.
     *   @param  s  String to alter.
     *   @param  f  String to look for.
     *   @param  r  String to replace it with, or null to just remove it.
     */
    public static String replace(String s, String f, String r) {
        if (s == null) {
            return s;
        }
        if (f == null) {
            return s;
        }
        if (r == null) {
            r = "";
        }
        int index01 = s.indexOf(f);
        while (index01 != -1) {
            s = s.substring(0, index01) + r + s.substring(index01 + f.length());
            index01 += r.length();
            index01 = s.indexOf(f, index01);
        }
        return s;
    }
}