package Parsing.YumlPrimitives;

import Parsing.GeneralPrimitives.Token;
import Parsing.GeneralPrimitives.Nothing;
import MDELite.ParsE;
import Parsing.Parsers.LineToParse;

/**
 * is a sequence of characters whose endpoint is a barrier, but does not include
 * barrier
 */
public class BoxEntry extends Token {

    /**
     * Standard constructor
     */
    public BoxEntry() {
    }

    /**
     * Standard constructor with line to parse
     *
     * @param l -- line to parse
     */
    public BoxEntry(LineToParse l) {
        super(l);
    }

    @Override
    public boolean canParse() {
        toBeParsed.AssertNotAtEnd(this);
        return true;
    }

    @Override
    public void consumeToken() {
        int i;
        boolean pastEnd = true;

        for (i = 0; i < toBeParsed.toParse.length(); i++) {
            char c = toBeParsed.toParse.charAt(i);
            if (!toBeParsed.allButRBracketOrBarrier(c)) {  // terminating char
                pastEnd = false;
                break;
            }
        }
        if (!pastEnd) {
            if (i == 0) {
                toBeParsed.token = Nothing.tok;
            } else {
                toBeParsed.returnSave(i);
            }
//           if (toBeParsed.toParse.startsWith("|")) {
//              toBeParsed.toParse = toBeParsed.toParse.substring(1); // skip over barrier
//           }
            return;
        }
        throw ParsE.toss(ParsE.runAwayEntry, toBeParsed.lineno, toBeParsed.toParse);
    }

    @Override
    public void parse() {
        toBeParsed.parseSave(this);
    }

    @Override
    public BoxEntry klone(LineToParse l) {
        return new BoxEntry(l);
    }

    @Override
    public String getEmsg() {
        return "box entry";
    }
}
