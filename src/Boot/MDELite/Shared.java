package Boot.MDELite;

import PrologDB.ErrorReport;
import PrologDB.Table;
import PrologDB.Tuple;
import java.util.LinkedList;

public class Shared {

    public static LinkedList<String> inputs;
    public static LinkedList<String> nonTempOutputs;
    public static LinkedList<String> outputs;

    public static void computeInOut(String path, Table arrow, Table domain) {
        String[] paths = path.split(MetaConform.pathSeparator);
        inputs = new LinkedList<>();
        outputs = new LinkedList<>();
        nonTempOutputs = new LinkedList<>();
        ErrorReport er = new ErrorReport();
        for (String p : paths) {
            Tuple a = arrow.getFirstEH((Tuple t) -> t.is("name", p));
            String[] sig = a.get("domainInputs").split(",");
            for (String s : sig) {
                if (inputs.contains(s) || outputs.contains(s)) {
                    continue;
                }
                inputs.add(s);
                // now check -- the inputs should not include temporary results
                boolean tmp = domain.getFirstEH((Tuple t) -> t.is("name", s)).is("temp", "true");
                if (tmp) {
                    er.add("input from temporary domain %s is required", s);
                }
            }
            String out = a.get("domainOutput");
            outputs.add(out);
            if (domain.getFirstEH((Tuple t) -> t.is("name", out)).is("temp", "false")) {
                nonTempOutputs.add(out);
            }
        }
        // now check -- the inputs should not include temporary results
        er.printReportEH(System.out);
    }
    
}
