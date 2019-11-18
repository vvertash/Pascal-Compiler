import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class IdentifierKeywordUtils {

    private static List<String> keywords = Arrays.asList("and", "begin", "boolean",
            "break", "byte", "continue", "div", "do", "double", "else", "end",
            "false", "if", "int64", "longint", "longword", "mod", "not",
            "or", "qword", "repeat", "shl", "shortint", "shr", "single", "smallint", "then",
            "true", "unit64", "until", "while", "word", "xor", "var", "string", "real", "for","to");
    private static HashSet<String> keywordsSet = new HashSet<String>(keywords);

    /**
     * checks if beginning of the string can be identifier
     * that means first symbol of the string is:
     * a printable character, not a digit, not a delimiter and not a '$' (reserved symbol)
     * @param str
     * @return true if this character can be begging of the identifier
     */
    public static boolean isIdentifierStart(String str) {
        char c = str.charAt(0);
        return isPrintableChar(c)
                && !(DelimiterUtils.isDelimiter(c) || Character.isDigit(c) || c == '$');
    }

    /**
     * chacks if character can be symbol in the middle of the identifier
     * that means - it is a printable character, not a delimiter and not a '$' (reserved symbol)
     * @param c
     * @return true if this character can be in the middle of the identifier
     */
    private static boolean isIdentifierMiddle(char c) { return isPrintableChar(c)
            && !DelimiterUtils.isDelimiter(c);}

    /**
     * processes identifier from the begibning og the current line
     * assumption: first character of the currentLine is an beginning of the identifier
     * @param currentLine
     * @return Identifier or Keyword token
     */
    public static Token processIdentifier(String currentLine) {
        String currentTokenBuffer = currentLine.substring(0,1);
        int index = 1;
        while (index < currentLine.length() &&
                isIdentifierMiddle(currentLine.charAt(index)) ){
            currentTokenBuffer = currentTokenBuffer+currentLine.charAt(index);
            index++;
        }
        if (isKeyword(currentTokenBuffer)){
            return new Token(currentTokenBuffer, Token.KEYWORD);
        }
        return new Token(currentTokenBuffer, Token.IDENTIFIER);
    }

    /**
     * checks if identifier is an keywrod
     * @param identifier
     * @return
     */
    private static boolean isKeyword(String identifier) {
        return keywordsSet.contains(identifier);
    }

    /**
     * checks if beginning of the string is an strange identifier
     * @param str
     * @return
     */
    public static boolean isStartOfStrangeIdentifier(char str){
        return str == '`';
    }

    /**
     * processes strange identifier from the beginning of the string
     * assumption: beginning of the current line string is an `
     * @param currentLine
     * @return Identifier token
     * @throws Exception
     */
    public static Token processStrangeIdentifier(String currentLine) throws Exception {
        String currentTokenBuffer = currentLine.substring(0,1);
        int index = 1;
        if (currentLine.length() > 1 && isIdentifierMiddle(currentLine.charAt(index))){
            currentTokenBuffer+=currentLine.charAt(index);
            index++;
        } else {
            throw new Exception("Incorrect strange identifier: " + currentTokenBuffer);
        }
        while (true){
            if (currentLine.length() > index) {
                currentTokenBuffer+=currentLine.charAt(index);
                if(currentLine.charAt(index) == '`') {
                    if (keywordsSet.contains(currentTokenBuffer.substring(1, currentTokenBuffer.length()-1))) {
                        return new Token(currentTokenBuffer, Token.IDENTIFIER);
                    } else {
                        throw  new Exception("Incorrect strange identifier: " + currentTokenBuffer);
                    }
                }
                index++;
            } else {
                throw new Exception("Incorrect strange identifier: " + currentTokenBuffer);
            }
        }
    }

    /**
     * checks if character is printable
     * @param c
     * @return
     */
    private static boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }
}
