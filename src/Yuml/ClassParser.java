package Yuml;

import MDELite.Marquee2Arguments;
import MDELite.ParsE;
import MDELite.Utils;
import Parsing.Parsers.YumlLineParser;
import PrologDB.DB;
import PrologDB.DBSchema;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class ClassParser {

    public static void main(String... args) {
        // Step 1: usual marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(ClassParser.class, ".yuml.yuml", ".ypl.pl", args);
        String inputFileName = mark.getInputFileName();
        PrintStream out = mark.getOutputPrintStream();

        // Step 2: initialize yuml database
        DBSchema dbs = DBSchema.readSchema(Utils.MDELiteHome()+"libpl/ypl.schema.pl");
        String[] path = Utils.parseFileName(inputFileName);
        String name = Utils.getName(path);

        DB db = new DB(name, dbs);

        // Step 3: standard loop read l, process l
        YumlLineParser.init();
        FileReader fr;
        int linenum = 1;
        try {
            fr = new FileReader(inputFileName);
            BufferedReader in = new BufferedReader(fr);

            while (true) {
                String line = in.readLine();
                if (line == null) {
                    in.close();
                    break;
                }
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }

                db = YumlLineParser.parseYumlLine(line, linenum++, db);
            }
        } catch (IOException ex) {
            throw ParsE.toss(ParsE.ioerror, linenum, ex.getMessage());
        }

        // Step 4: output everything.
        db.print(out);
        out.close();
    }
}
