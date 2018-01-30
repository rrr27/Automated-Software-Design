package Boot.MDELite;

import MDELite.Marquee2Arguments;
import MDELite.RunningBear;
import static MDELite.RunningBear.RBSetup;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.LinkedList;

public class meta2java extends RunningBear {

    static Table domain, arrow, path;
    static String appName;

    public static void main(String... args) {
        // Step 1: standard marquee processing
        Marquee2Arguments mark = new Marquee2Arguments(meta2java.class, ".meta.pl", ".java", args);
        RBSetup(mark, args);
        appName = mark.getAppName(args[0]);

        // Step 2: open tables;
        domain = db.getTableEH("domain");
        arrow = db.getTableEH("arrow");
        path = db.getTableEH("path");

        // Step 3: generate code
        header();
        domains();
        arrows();
        maps();
        paths();
        l("}\n");
    }

    static void header() {
        l("package Tool;\n", appName);
        l("public class %s extends Boot.MDELite.Common {\n", appName);
        l("    public static void main(String... args) {");
        l("        main(%s.class, args, state, domains.values(), maps.values(),arrows.values());", appName);
        l("    }\n");
    }

    static void domains() {
        l("    static enum domains implements IEnum {");
        for (Tuple d : domain.tuples()) {
            String dname = d.get("name");
            if (d.is("ext", "")) {
                l("        %s(\".%s\"),", dname, dname);
            } else {
                l("        %s(\".%s.%s\"),", dname, dname, d.get("ext"));
            }
        }
        l("        ;\n");
        // stock code from this point on
        l("        public String data; // file type ending\n");
        l("        private domains(String end) {");
        l("            this.data = end;");
        l("        }\n");
        l("        @Override");
        l("        public String getEnd() {");
        l("            return data;");
        l("        }\n");
        l("        public static domains[] list(domains... args) {");
        l("            return args;");
        l("        }");
        l("    };\n");
    }

    static void maps() {
        l("    static enum maps implements PEnum {");
        for (Tuple p : path.tuples()) {
            String pname = p.get("name");
            l("        %s( new Triple(", pname);
            l("          a -> { %s.%s(a); return null; },", appName, pname);
            Shared.computeInOut(p.get("path"), arrow, domain);
            sigs(Shared.inputs);
            sigs(Shared.nonTempOutputs);
        }
        stockCode("maps");
    }

    static void sigs(LinkedList<String> from) {
        if (from.isEmpty()) {
            l("          new domains[0])),");
        } else {
            p("          domains.list(");
            String comma = "";
            for (String s : from) {
                p("%sdomains.%s", comma, s /*domName*/);
                comma = ",";
            }
            String finish = (from == Shared.inputs) ? ")," : "))),";
            l(finish);
        }
    }

    static void stockCode(String constructorName) {
        // stock code from this point on
        l("        ;\n");
        l("        private final Triple data;\n");
        l("        private %s(Triple data) { this.data = data;} \n", constructorName);
        l("        @Override");
        l("        public Triple get() { return data; }\n");
        l("        @Override");
        l("        public IEnum[] getArray(int size) { return new domains[size]; }");
        l("    }\n");
    }

    static void arrows() {
        l("    static enum arrows implements PEnum {");
        for (Tuple d : domain.tuples()) {
            String dname = d.get("name");
            if (d.is("ext", "pl")) {
                l("        %sConform( new Triple(", dname);
                l("          a -> { %s.main(a); return null; },", d.get("conformExecutable"));
                l("          domains.list(domains.%s),", dname);
                l("          new domains[0])),");
            }
        }
        for (Tuple a : arrow.tuples()) {
            l("        %s( new Triple(", a.get("name"));
            l("          a -> { %s.main(a); return null; },", a.get("javaExecutable"));
            String[] sig = a.get("domainInputs").split(MetaConform.domainSeparator);
            p("          domains.list(");
            String comma = "";
            for (String s : sig) {
                p("%sdomains.%s", comma, s);
                comma = ",";
            }
            l("),");
            l("          domains.list(domains.%s))),", a.get("domainOutput"));
        }
        stockCode("arrows");
    }

    static void paths() {
        for (Tuple p : path.tuples()) {
            singlePath(p);
        }
    }

    static void singlePath(Tuple p) {
        String map = p.get("name");
        l("    public static void %s(String[] args) {", map);
        l("        enoughCommandLineArguments(maps.%s, args);\n", map);
        String pth = p.get("path");
        String[] steps = pth.split(MetaConform.pathSeparator);
        for (String step : steps) {
            l("        invoke(arrows.%s, state);", step);
            Tuple a = arrow.getFirstEH(t -> t.is("name", step));
            String resulttype = a.get("domainOutput");
            Tuple d = domain.getFirstEH(t -> t.is("name", resulttype));
            if (d.is("ext", "pl")) {
                l("        invoke(arrows.%sConform, state);", d.get("name"));
            }
        }
        l("        completed(maps.%s);", map);
        l("    }\n");
    }
}
