package MDELite;

/** mother of all enums in MDELite
 * @param <T> -- needed to satisfy Java compiler for inheritance
 */
public interface ErrInt<T> {
    /**
     * @return array enum values
     */
    ErrInt<T>[] vals();
    
    /**
     * @return the string name of an enum
     */
    String name();
    
    /**
     * @return the parameterized error message of an enum
     */
    String getMsg();
}
