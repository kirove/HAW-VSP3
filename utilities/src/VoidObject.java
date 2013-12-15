import java.io.Serializable;

/**
 * Created by Cenan on 12.12.13.
 */
public class VoidObject implements Serializable {
    private static VoidObject instance = new VoidObject();

    private VoidObject() {

    }

    public static VoidObject getInstance(){
        return instance;
    }

}
