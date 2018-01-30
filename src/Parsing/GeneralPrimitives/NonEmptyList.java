package Parsing.GeneralPrimitives;

import MDELite.ParsE;
import Parsing.Parsers.LineToParse;

/** NonEmptyList is a sequence of "node ( comma node )*" */
public class NonEmptyList extends Token { // comma separated tuple values

    Token node;
    Token comma;

    /** Standard constructor
     * @param node -- token that appears in a list
     * @param comma -- token separator that is used
     */
    public NonEmptyList(Token node, Token comma) {
        this.node = node;
        this.comma = comma;
    }
    
    /** Standard constructor with line to parse
     * @param node -- token that appears in a list
     * @param comma -- token separator that is used
     * @param l -- line to parse
     */
    public NonEmptyList(Token node, Token comma, LineToParse l) {
        super(l);
        this.node = node;
        this.comma = comma;
    }

    Token next;
    
    @Override
    public boolean canParse() {
        toBeParsed.AssertNotAtEnd(this);
        next = node.klone(toBeParsed);
        return next.canParse();
    }

    @Override
    public void consumeToken() {
        next.consumeToken();
    }

    @Override
    public void parse() { // remember: canParse was called before parse
//        toBeParsed.AssertNotAtEnd(this);

        // always parse the first node, from then it can be optional
        toBeParsed.parseSaveQualified(next);

        Token c = comma.klone(toBeParsed);
        if (c.canParse()) {
            toBeParsed.parseSkip(c);
            next = node.klone(toBeParsed);
            if (next.canParse()) {
                parse();
            } else {
                throw ParsE.toss(ParsE.parseError, toBeParsed.lineno, next.getEmsg() + " expected at >>" + toBeParsed.toParse);
            }
        }
    }

    @Override
    public NonEmptyList klone(LineToParse l) {
        return new NonEmptyList(node, comma, l);
    }

    @Override
    public String getEmsg() {
        return node.getEmsg();
    }
}
