package MDL;
import Violett.ClassConform;
import Violett.StateParser;
import Violett.StateUnParser;
import Violett.ClassParser;
import Violett.ClassUnParser;
import Violett.StateConform;


/** every program in MDELite */
public class All {
    // all MDL programs (not packages)
    static Class[] all = { All.class, Catalina.class, InstanceOf.class, OO2schema.class, 
        ReadDB.class, ReadTable.class, ReadSchema.class, VerifyInstall.class, Version.class, Violet.class,
        ClassParser.class,ClassUnParser.class, 
        StateParser.class, StateUnParser.class, 
        Yuml.ClassConform.class, Yuml.ClassParser.class, Yuml.ClassUnParser.class  };
        
    /**
     * enumerates every MDELite program
     * @param args -- ignored
     */
    public static void main(String... args) {
        for (Class c : all) {
            System.out.format("%s\n",c.getName());
        }
    }
}
