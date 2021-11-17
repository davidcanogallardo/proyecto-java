package pr5;

import java.util.HashMap;

public interface Persistable <T> {
    public abstract T add(T obj);
    public abstract T delete(T id);
    public abstract T get(T id);
    public abstract HashMap<Integer, T> getMap();
}
