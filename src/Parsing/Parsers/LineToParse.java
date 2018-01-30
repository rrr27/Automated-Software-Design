package Parsing.Parsers;

import Parsing.GeneralPrimitives.Token;
import java.util.LinkedList;
import MDELite.ParsE;

/**
 * a LineToParse is a line to be parsed (the String of a line + its line number)
 */
public class LineToParse extends PrimitiveLookAhead {

    /**
     * toParse is the unparsed segment/string 
     */
    public String toParse;

    /**
     * most recently parsed token (some are skipped)
     */
    public String token;

    /**
     * parseList is a linkedList of tokens (strings) that capture the essence of
     * a parse
     */
    public LinkedList<String> parseList;

    /**
     * lineno is the line number of the string, toParse
     */
    public int lineno =-1;
    
    /**
     * 
     * @return line number of LineToParse
     */
    public int getLineNo() {
        return lineno;
    }

    /**
     * parse lineContents that has lineNumber
     * @param lineContents  -- contents of line to parse
     * @param lineNumber -- number of line in file (-1 otherwise)
     */
    public LineToParse(String lineContents, int lineNumber) {
        toParse = lineContents;
        token = null;
        parseList = new LinkedList<>();
        lineno = lineNumber;
    }

    /**
     * @return next character to parse
     */
    public char peek() {
        return toParse.charAt(0);
    }

    /**
     * @param s -- string look-ahead
     * @return true if unparsed segment of line starts with s
     */
    public boolean startsWith(String s) {
        return toParse.startsWith(s);
    }

    /**
     * 
     * @param i index of character lookahead in unparsed segment of line
     * @return ith character into unparsed string
     */
    public char charAt(int i) {
        return toParse.charAt(i);
    }

    
    /**
     * @return true if nothing left to parse.
     */
    public boolean atEnd() {
        return toParse.equals("");
    }

    /**
     * is there more to parse to parse? If not, throw ParseException
     * @param t -- token that was expected to be read
     */
    public void AssertNotAtEnd(Token t) {
        if (toParse.equals("")) {
            throw ParsE.toss(ParsE.streamEndReached, lineno, t.getEmsg() );
        }
    }

    /**
     * advance toParse by the length i of a parsed "token" and set token to the
     * length i string that has been removed; this is the "result" of parsing.
     * Return true.
     *
     * @param i -- length of parsed "token" string
     * @return true
     */
    public boolean returnSave(int i) {
        token = toParse.substring(0, i).trim();
        toParse = toParse.substring(i).trim();
        return true;
    }
    
    public boolean returnSaveTrim(int i) {
        token = toParse.substring(1, i-1).trim();
        toParse = toParse.substring(i).trim();
        return true;
    }
    
    /**
     * advance toParse by the length i of a parsed "token"; no token is
     * returned; return true.
     *
     * @param i -- length of parsed token
     * @return true
     */
    public boolean returnSkip(int i) {
        toParse = toParse.substring(i).trim();
        return true;
    }

    /**
     * assuming canParse() == true, consume token t and add parsed token to the parseList
     * @param t -- token to consume
     */
    public void parseSaveQualified(Token t) {
        t.consumeToken();
        parseList.add(token);
    }

    /**
     * consume expected token t and add parsed token to the parseList
     * @param t -- token to consume
     */
    public void parseSave(Token t) {
        if (t.canParse()) {
            t.consumeToken();
            parseList.add(token);
        } else {
            throw ParsE.toss(ParsE.parseError, lineno, t.getEmsg() + " expected at >>" + toParse);
        }
    }

    /**
     * assuming canParse() == true, consume token t and ignore its existence
     * @param t -- token to consume
     */
    public void parseSkipQualified(Token t) {
        t.consumeToken();
    }

    /** consume expected token t and ignore its existence
     * @param t -- token to consume
     */
    public void parseSkip(Token t) {
        if (t.canParse()) {
            t.consumeToken();
        } else {
            throw ParsE.toss(ParsE.parseError, lineno, t.getEmsg() + " expected at >>" + toParse);
        }
    }
    
    /** used rarely (Barrier/Nothing) to add indicators to the parseList 
     * @param s -- string to add
     */
    public void add2ParseList(String s) {
        parseList.add(s);
    }
}
