package Tool;

public class catalina extends Boot.MDELite.Common {

    public static void main(String... args) {
        main(catalina.class, args, state, domains.values(), maps.values(),arrows.values());
    }

    static enum domains implements IEnum {
        state(".state.violet"),
        fsm(".fsm.pl"),
        meta(".meta.pl"),
        java(".java"),
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
        fsmConform( new Triple(
          a -> { Boot.MDELite.CategoryConform.main(a); return null; },
          domains.list(domains.fsm),
          new domains[0])),
        metaConform( new Triple(
          a -> { Boot.MDELite.MetaConform.main(a); return null; },
          domains.list(domains.meta),
          new domains[0])),
        parse( new Triple(
          a -> { Violett.StateParser.main(a); return null; },
          domains.list(domains.state),
          domains.list(domains.fsm))),
        m2m( new Triple(
          a -> { Boot.MDELite.fsm2meta.main(a); return null; },
          domains.list(domains.fsm),
          domains.list(domains.meta))),
        m2t( new Triple(
          a -> { Boot.MDELite.meta2java.main(a); return null; },
          domains.list(domains.meta),
          domains.list(domains.java))),
        ;

        private final Triple data;

        private arrows(Triple data) { this.data = data;} 

        @Override
        public Triple get() { return data; }

        @Override
        public IEnum[] getArray(int size) { return new domains[size]; }
    }

    static enum maps implements PEnum {
        all( new Triple(
          a -> { catalina.all(a); return null; },
          domains.list(domains.state),
          domains.list(domains.meta,domains.java))),
        validateViolet( new Triple(
          a -> { catalina.validateViolet(a); return null; },
          domains.list(domains.state),
          new domains[0])),
        validateMeta( new Triple(
          a -> { catalina.validateMeta(a); return null; },
          domains.list(domains.state),
          domains.list(domains.meta))),
        quick( new Triple(
          a -> { catalina.quick(a); return null; },
          domains.list(domains.meta),
          domains.list(domains.java))),
        ;

        private final Triple data;

        private maps(Triple data) { this.data = data;} 

        @Override
        public Triple get() { return data; }

        @Override
        public IEnum[] getArray(int size) { return new domains[size]; }
    }

    public static void all(String[] args) {
        enoughCommandLineArguments(maps.all, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        invoke(arrows.m2m, state);
        invoke(arrows.metaConform, state);
        invoke(arrows.m2t, state);
        completed(maps.all);
    }

    public static void validateViolet(String[] args) {
        enoughCommandLineArguments(maps.validateViolet, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        completed(maps.validateViolet);
    }

    public static void validateMeta(String[] args) {
        enoughCommandLineArguments(maps.validateMeta, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        invoke(arrows.m2m, state);
        invoke(arrows.metaConform, state);
        completed(maps.validateMeta);
    }

    public static void quick(String[] args) {
        enoughCommandLineArguments(maps.quick, args);

        invoke(arrows.m2t, state);
        completed(maps.quick);
    }

}

