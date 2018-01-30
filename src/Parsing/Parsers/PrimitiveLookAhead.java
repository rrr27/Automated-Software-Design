package Parsing.Parsers;

/**
 * these are primitive lookahead tests in a prolog parser
 */
public class PrimitiveLookAhead {

    /**
     * is c a digit?
     * @param c -- character to examine
     * @return is c a digit?
     */
    public boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }
    
    /**
     * is the start of string s a number?
     * @param s -- string to examine
     * @return -- true if the start of s is a number
     */
    public boolean isNumber(String s)  {
        if (s.length()<2)
            return false;
        char first = s.charAt(0);
        char second = s.charAt(1);
        if (isDigit(first)) {
            return true;
        } else
        return isNegative(first) && isDigit(second);
    }
    
    /**
     * is c a double-quote or a single-quote?
     * @param c -- character to examine
     * @return is c a double-quote?
     */
    public boolean isQuote(char c) {
         return c == '"' || c == '\'';
    }

    /**
     * is c a letter?
     * @param c -- character to examine
     * @return is c a letter?
     */
    public boolean isLetter(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }
    
    /**
     * is c a letter or digit?
     * @param c -- character to examine
     * @return is c a letter or digit?
     */
    public boolean isLetterOrDigit(char c) {
        return isLetter(c) || isDigit(c);
    }

    /**
     * is c a letter or digit or underscore?
     * @param c -- character to examine
     * @return is c a letter or digit or underscore?
     */
    public boolean isLetterOrDigitOrUnderScore(char c) {
        return isLetter(c) || isDigit(c) || isUnderScore(c);
    }
    
    /**
     * is c a letter or digit or underscore?
     * @param c -- character to examine
     * @return is c a letter or digit or underscore?
     */
    public boolean isColumnNameChar(char c) {
        return isLetter(c) || isDigit(c) || isUnderScore(c) || c==':';
    }

    /**
     * is c a minus (negative) sign?
     * @param c -- character to examine
     * @return is c a minus (negative) sign?
     */
    public boolean isNegative(char c) {
        return c == '-';
    }

    /**
     * is c a lowercase letter?
     * @param c -- character to examine
     * @return is c a lowercase letter?
     */
    public boolean isLowercaseLetter(char c) {
        return 'a' <= c && c <= 'z';
    }
    
    /**
     * is c a dot?
     * @param c -- character to examine
     * @return is c a dot?
     */
    public boolean isDot(char c) {
        return c == '.';
    }
    
    /**
     * is c an underscore?
     * @param c -- character to examine
     * @return is c an underscore?
     */
    public boolean isUnderScore(char c) {
        return c == '_';
    }
    
    /**
     * anything but brackets or barrier -- strange token of Yuml
     * @param c -- char to test
     * @return true if anything but brackets or barrier
     */
    public boolean allButRBracketOrBarrier(char c) {
        return !(c==']' || c=='|');
    }
    
    /**
     * used by Yuml
     * @param c char to test
     * @return true if c == dot or star
     */
    public boolean isCard(char c) {
        return c=='.' || c=='*';
    }
    
    /**
     * used by Yuml
     * @param c  char to test
     * @return true if letter or digit or underscore or isCard or semi or blank
     */
    public boolean isRole(char c) {
        return isLetterOrDigitOrUnderScore(c) || isCard(c) 
                || c==';' || c==' ';
    }
}
