package Boot.MDELite;

import MDELite.Utils;
import java.util.HashMap;
import java.util.function.UnaryOperator;

public abstract class Common {

    public static HashMap<String, String> state = new HashMap<>();

    public static class Triple {

        public final UnaryOperator<String[]> method;
        public final IEnum[] inputs;
        public final IEnum[] output;

        public Triple(UnaryOperator<String[]> method, IEnum[] inputs, IEnum[] output) {
            this.method = method;
            this.inputs = inputs;
            this.output = output;
        }
    }

    public static interface IEnum {

        String getEnd();

        String name();
    }

    public static interface PEnum {

        Triple get();

        String name();

        IEnum[] getArray(int size);
    }

    public static IEnum[] getAllArgTypes(PEnum e) {
        IEnum[] inputs = e.get().inputs;
        IEnum[] output = e.get().output;
        int ilen = inputs.length;
        IEnum[] result = e.getArray(ilen + output.length);
        for (int i = 0; i < inputs.length; i++) {
            result[i] = inputs[i];
        }
        for (int i = 0; i < output.length; i++) {
            result[ilen + i] = output[0];
        }
        return result;
    }

    // values set by main
    static Class c;
    static PEnum[] mmaps;
    static String AppName;

    public static void marquee(String msg) {
        if (msg != null) {
            System.out.format("   -----> %s\n\n", msg);
        }

        System.out.format("Usage: %s option args...\n", c.getName());
        for (PEnum o : mmaps) {
            Triple t = o.get();
            String inputs = makeStr(t.inputs);
            String outputs = makeStr(t.output);
            if (outputs.equals(""))
                outputs = "T/F";
            System.out.format("       %-15s   %s -> %s\n", o, inputs, outputs);
        }
        System.exit(0);
    }

    private static String makeStr(IEnum[] l) {
        String result = "";
        String comma = "";
        for (IEnum s : l) {
            result = result + comma + s.name();
            comma = ",";
        }
        return result;
    }

    public static void main(Class c, String[] args, HashMap<String, String> state, IEnum[] domains,
            PEnum[] maps, PEnum[] arrowValues) {
        Common.c = c;
        Common.mmaps = maps;
        String map2Invoke = LoadArgumentsIntoState(args, state, domains);
        Common.AppName = Utils.getName(Utils.parseFileName(args[1]));

        // now invoke application method
        for (PEnum m : maps) {
            if (m.name().equals(map2Invoke)) {
                m.get().method.apply(args);
                return;
            }
        }
        marquee("unrecognizable command '" + map2Invoke + "'");
    }

    public static void completed(PEnum option) {
        System.out.format("       %s %s -> %s .....  completed!\n", option.name(),
                toString(option.get().inputs),
                toString(option.get().output));
    }

    public static void enoughCommandLineArguments(PEnum e, String[] args) {
        IEnum[] inputs = e.get().inputs;
        if ((args.length - 1) != inputs.length) {
            String msg = String.format("%d file arguments expected for option %s, only %d were given\n", inputs.length, e.name(), args.length - 1);
            marquee(msg);
        }
    }

    public static void invoke(PEnum m, HashMap<String, String> state) {
        IEnum[] argtypes = getAllArgTypes(m);
        String[] arguments = SetArguments(state, argtypes);

        try {
        m.get().method.apply(arguments);
        }
        catch (Exception e) {
            System.out.format("Error when invoking %s\n",m.name());
            throw e;
        }
        IEnum[] out = m.get().output;
        IEnum[] in = m.get().inputs;
        for (int i = 0; i < out.length; i++) {
            IEnum e = m.get().output[i];
            state.put(e.name(), arguments[in.length + i]);
        }
    }

    static String toString(IEnum[] array) {
        String result = "";
        for (IEnum d : array) {
            result = result + d + " ";
        }
        return result;
    }

    private static String LoadArgumentsIntoState(String[] args,
            HashMap<String, String> state, IEnum[] values) {
        if (args.length < 2) {
            marquee(null);
        }
        for (int i = 1; i < args.length; i++) {
            boolean recognized = false;
            for (IEnum d : values) {
                if (args[i].endsWith(d.getEnd())) {
                    state.put(d.name(), args[i]);
                    recognized = true;
                    break;
                }
            }
            if (!recognized) {
                throw BootError.toss(BootError.unrecognizedCommandLineArg, args[i]);
            }
        }
        return args[0]; // return command-line map to invote
    }

    private static String[] SetArguments(HashMap<String, String> state, IEnum... argTypes) {
        int len = argTypes.length;
        String[] args = new String[len];
        for (int i = 0; i < len - 1; i++) {
            String at = state.get(argTypes[i].name());
            if (at == null) {
                throw BootError.toss(BootError.domainHasNullCurrencyValue);
            }
            args[i] = at; ///AppName + argTypes[i].getEnd();
        }
        args[len - 1] = AppName + argTypes[len - 1].getEnd();
        return args;
    }
}
