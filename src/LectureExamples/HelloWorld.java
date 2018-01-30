package LectureExamples;

import MDELite.Marquee1Argument;

public class HelloWorld extends MDELite.RunningBear {
    public static void main(String... args) {
        new Marquee1Argument(HelloWorld.class,"",args);
        l("hello world!");
        l("this is rima calling!");
    }
}
