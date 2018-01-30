package Tool;

public class fsm extends Boot.MDELite.Common {

    public static void main(String... args) {
        main(fsm.class, args, state, domains.values(), maps.values(),arrows.values());
    }

    static enum domains implements IEnum {
        state(".state.violet"),
        fsm(".fsm.pl"),
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
        parse( new Triple(
          a -> { Violett.StateParser.main(a); return null; },
          domains.list(domains.state),
          domains.list(domains.fsm))),
        NestSwitch( new Triple(
          a -> { Boot.fsm.kevinFsm2java.main(a); return null; },
          domains.list(domains.fsm),
          domains.list(domains.java))),
        DesignPattern( new Triple(
          a -> { Boot.fsm.sriramFsm2java.main(a); return null; },
          domains.list(domains.fsm),
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
        convertDP( new Triple(
          a -> { fsm.convertDP(a); return null; },
          domains.list(domains.state),
          domains.list(domains.java))),
        convertNS( new Triple(
          a -> { fsm.convertNS(a); return null; },
          domains.list(domains.state),
          domains.list(domains.java))),
        validate( new Triple(
          a -> { fsm.validate(a); return null; },
          domains.list(domains.state),
          new domains[0])),
        ;

        private final Triple data;

        private maps(Triple data) { this.data = data;} 

        @Override
        public Triple get() { return data; }

        @Override
        public IEnum[] getArray(int size) { return new domains[size]; }
    }

    public static void convertDP(String[] args) {
        enoughCommandLineArguments(maps.convertDP, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        invoke(arrows.DesignPattern, state);
        completed(maps.convertDP);
    }

    public static void convertNS(String[] args) {
        enoughCommandLineArguments(maps.convertNS, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        invoke(arrows.NestSwitch, state);
        completed(maps.convertNS);
    }

    public static void validate(String[] args) {
        enoughCommandLineArguments(maps.validate, args);

        invoke(arrows.parse, state);
        invoke(arrows.fsmConform, state);
        completed(maps.validate);
    }

}

