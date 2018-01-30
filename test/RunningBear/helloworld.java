package RunningBear;

public class helloworld extends MDELite.RunningBear {
    public static void main(String... args) throws Exception {
        MDELite.Marquee2Arguments mark = new MDELite.Marquee2Arguments(helloworld.class,"","",args);
        RBSetup(mark, args);
        l("hello world!");
        l();
        l("this is don calling!");
    }
}


