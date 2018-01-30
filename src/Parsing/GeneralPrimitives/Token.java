package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;

/** a token is a primitive unit of parsing */
public abstract class Token {
    
    /**
     * string to be parsed
     */
    protected LineToParse toBeParsed;
    
    /** Standard constructor */
    protected Token() { }
    
    /** Standard constructor with line to parse
     * @param l -- line to parse
     */
    protected Token(LineToParse l) {
        toBeParsed = l;
    }
    
    /**
     * can this token be parsed?  is the next character
     * of toBeParsed consistent with the requested parsing task?
     * no changes to LineToParse are made
     * @return true if the next character to parse is consistent with requested parsing task
     */
    public abstract boolean canParse();
    
    /** consume the next token if canParse()==true ; token is stored
     * in LinetoParse object */
    public abstract void consumeToken();

    /** actually parse toBeParsed given requested parsing task */
    public abstract void parse();
    
    /**
     * set line to parse (l)
     * @param l -- the LineToParse object
     */
    public void setLineToParse(LineToParse l) {
       toBeParsed = l;
    }
    
    /** return copy of 'this'
     * @param l -- line to parse
     * @return  -- token object to parse this line
     */
    public abstract Token klone(LineToParse l);
    
    /** 
     * @return  -- string concept that is to be parsed*/
    public abstract String getEmsg();
}
