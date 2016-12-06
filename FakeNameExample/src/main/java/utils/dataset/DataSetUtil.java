/**
 * 
 */
package utils.dataset;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jayesh
 *
 */
public class DataSetUtil {
    public static Map<Integer, Character> INT_TO_CHAR = getIntToChar();
    public static Map<Character, Integer> CHAR_TO_INT = getCharToInt();
    public static final int N_CHARS = INT_TO_CHAR.size();
    public static int nOut = CHAR_TO_INT.size();

    public static Map<Integer, Character> getIntToChar() {
        Map<Integer, Character> map = new HashMap<>();
        char[] chars = getValidCharacters();
        for (int i = 0; i < chars.length; i++) {
            map.put(i, chars[i]);
        }
        return map;
    }

    public static Map<Character, Integer> getCharToInt() {
        Map<Character, Integer> map = new HashMap<>();
        char[] chars = getValidCharacters();
        for (int i = 0; i < chars.length; i++) {
            map.put(chars[i], i);
        }
        return map;
    }

    /**
     * A minimal character set, with a-z, A-Z, 0-9 and common punctuation etc
     */
    public static char[] getValidCharacters() {
        List<Character> validChars = new LinkedList<>();
        for (char c = 'a'; c <= 'z'; c++) validChars.add(c);
        for (char c = 'A'; c <= 'Z'; c++) validChars.add(c);
        for (char c = '0'; c <= '9'; c++) validChars.add(c);
        char[] temp = {'!', '&', '(', ')', '?', '-', '\'', '"', ',', '.', ':', ';', ' ', '\n', '\t'};
        for (char c : temp) validChars.add(c);
        char[] out = new char[validChars.size()];
        int i = 0;
        for (Character c : validChars) out[i++] = c;
        return out;
    }

}
