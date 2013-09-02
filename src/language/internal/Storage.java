package language.internal;

import java.util.List;
import language.value.Value;

public interface Storage {

    public Value put(String key, Value value);

    public Value set(String key, Value value);
    
    public Value get(String key);
    
    public List nodes();
}
