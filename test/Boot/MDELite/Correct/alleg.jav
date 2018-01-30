package Tool;

public class alleg extends Boot.MDELite.Common {

    public static void main(String... args) {
        main(alleg.class, args, state, domains.values(), maps.values(),arrows.values());
    }

    static enum domains implements IEnum {
        Class(".Class.violet"),
        Vpl(".Vpl.pl"),
        Schema(".Schema.pl"),
        Java(".Java"),
        ;

        public String data; // file type ending

        private domains(String end) {
            this.data = end;
        }

        @Override
        public String getEnd() {
            return data;
        }

        public static domains[] list(domains... args) {
            return args;
        }
    };

    static enum arrows implements PEnum {
        VplConform( new Triple(
          a -> { Boot.allegory.ClassConform.main(a); return null; },
          domains.list(domains.Vpl),
          new domains[0])),
        SchemaConform( new Triple(
          a -> { MDL.ReadSchema.main(a); return null; },
          domains.list(domains.Schema),
          new domains[0])),
        parse( new Triple(
          a -> { Violett.ClassParser.main(a); return null; },
          domains.list(domains.Class),
          domains.list(domains.Vpl))),
        toSchema( new Triple(
          a -> { Boot.allegory.Vpl2Schema.main(a); return null; },
          domains.list(domains.Vpl),
          domains.list(domains.Schema))),
        toJava( new Triple(
          a -> { Boot.allegory.VplSchema2Java.main(a); return null; },
          domains.list(domains.Schema),
          domains.list(domains.Java))),
        ;

        private final Triple data;

        private arrows(Triple data) { this.data = data;} 

        @Override
        public Triple get() { return data; }

        @Override
        public IEnum[] getArray(int size) { return new domains[size]; }
    }

    static enum maps implements PEnum {
        validate( new Triple(
          a -> { alleg.validate(a); return null; },
          domains.list(domains.Class),
          domains.list(domains.Vpl))),
        onlySchema( new Triple(
          a -> { alleg.onlySchema(a); return null; },
          domains.list(domains.Class),
          domains.list(domains.Vpl,domains.Schema))),
        full( new Triple(
          a -> { alleg.full(a); return null; },
          domains.list(domains.Class),
          domains.list(domains.Vpl,domains.Schema,domains.Java))),
        fromSchema( new Triple(
          a -> { alleg.fromSchema(a); return null; },
          domains.list(domains.Schema),
          domains.list(domains.Java))),
        ;

        private final Triple data;

        private maps(Triple data) { this.data = data;} 

        @Override
        public Triple get() { return data; }

        @Override
        public IEnum[] getArray(int size) { return new domains[size]; }
    }

    public static void validate(String[] args) {
        enoughCommandLineArguments(maps.validate, args);

        invoke(arrows.parse, state);
        invoke(arrows.VplConform, state);
        completed(maps.validate);
    }

    public static void onlySchema(String[] args) {
        enoughCommandLineArguments(maps.onlySchema, args);

        invoke(arrows.parse, state);
        invoke(arrows.VplConform, state);
        invoke(arrows.toSchema, state);
        invoke(arrows.SchemaConform, state);
        completed(maps.onlySchema);
    }

    public static void full(String[] args) {
        enoughCommandLineArguments(maps.full, args);

        invoke(arrows.parse, state);
        invoke(arrows.VplConform, state);
        invoke(arrows.toSchema, state);
        invoke(arrows.SchemaConform, state);
        invoke(arrows.toJava, state);
        completed(maps.full);
    }

    public static void fromSchema(String[] args) {
        enoughCommandLineArguments(maps.fromSchema, args);

        invoke(arrows.toJava, state);
        completed(maps.fromSchema);
    }

}

