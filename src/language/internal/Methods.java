package language.internal;

import java.util.List;
import language.instruction.Method;

public interface Methods {

    public void put(String key, Method method);
    
    public Method get(String key);
    
    public List nodes();
}
