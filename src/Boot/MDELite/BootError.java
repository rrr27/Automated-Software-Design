package Boot.MDELite;

import MDELite.ErrInt;
import MDELite.Utils;

/** all run-time errors in MDELite */
public enum BootError implements ErrInt<BootError> {

    domainHasNullCurrencyValue("domain DOMAIN has null currency value"),
    unrecognizedCommandLineArg("unrecognized command line argument ARGUMENT"),
    ;

    private final String msg;

    /** 
     * @param msg -- string interpretation of error
     */
    BootError(String msg) {
        this.msg = msg;
    }
    
    /** get error message of error */
    @Override
    public String getMsg() { 
        return msg;
    }
    
    /**
     * @return the enums of Error in an array
     */
    @Override
    public ErrInt<BootError>[] vals() {
        return BootError.values();
    }
    
    /** this is the constructor for throwing an error
     * 
     * @param error -- which error 
     * @param args -- arguments of error message
     * @return RuntimeException
     */
    public static RuntimeException toss(BootError error, Object... args) {
        if (error.msg.contains("FILENAME")) { args[0] = Utils.shortFileName((String) args[0]); }
        return new RuntimeException(MDELite.Utils.makeString(error.msg,args));
    }  
}
