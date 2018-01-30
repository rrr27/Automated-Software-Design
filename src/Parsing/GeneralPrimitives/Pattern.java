package Parsing.GeneralPrimitives;

import MDELite.ParsE;
import Parsing.Parsers.LineToParse;

/** pattern is a sequence of tokens to parse */
public class Pattern extends Token {

    private Token[] sequence;

    /** Standard constructor
     * @param array -- sequence of tokens to be parsed
     */
    public Pattern(Token[] array) {
        super();
        this.sequence = array;
        if (array.length == 0) {
            throw ParsE.toss(ParsE.parseError, -1);
        }
    }

    /** Standard constructor with line to parse
     *
     * @param l -- line to parse
     */
    public Pattern(LineToParse l) {
        super(l);
    }

    private Token first;

    @Override
    public boolean canParse() {
        first = sequence[0].klone(toBeParsed);
        return first.canParse();
    }

    @Override
    public void consumeToken() {
        first.consumeToken();
    }

    @Override
    public String getEmsg() {
        first = sequence[0].klone(toBeParsed);
        return first.getEmsg();
    }

    @Override
    public Pattern klone(LineToParse l) {
        Pattern p = new Pattern(sequence);
        p.toBeParsed = l;
        return p;
    }

    @Override
    public void parse() {
        for (Token t : sequence) {
            t.setLineToParse(toBeParsed);
            if (t.canParse()) {
                t.parse();
            } else {
                if (toBeParsed.atEnd())
                    throw ParsE.toss(ParsE.streamEndReached, toBeParsed.getLineNo(), t.getEmsg());
                else 
                    throw ParsE.toss(ParsE.parseError, toBeParsed.getLineNo(), t.getEmsg() + " expected at >>" + toBeParsed.toParse);
            }
        }
    }
}
