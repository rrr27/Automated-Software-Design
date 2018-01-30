package Parsing.GeneralPrimitives;

import Parsing.Parsers.LineToParse;


/** parse fixed string and don't save result */
public abstract class FixedSkip extends Token {

    /**
     * this is the string of a token that is expected
     */
    String pattern;
    
    /** this is the string to report in an error */
    String emsg;
    
    /** Standard constructor */
    public FixedSkip() {
        pattern = getPattern();
        emsg = getEmsg();
    }
    
    /** Standard constructor with line to parse
     * 
     * @param l -- line to parse
     */
    public FixedSkip(LineToParse l) {
        super(l);
        pattern = getPattern();
        emsg = getEmsg();
    }
    
    /** 
     * @return fixed pattern*
     */
    public abstract String getPattern();
    
    @Override
    public boolean canParse() {
//        toBeParsed.AssertNotAtEnd(this);
        return toBeParsed.startsWith(pattern);
    }

    @Override
    public void consumeToken() {
        toBeParsed.returnSkip(pattern.length());
    }
    
    @Override
    public void parse() {
        toBeParsed.parseSkipQualified(this);
    }
    
    @Override
    public abstract FixedSkip klone(LineToParse l);
}
