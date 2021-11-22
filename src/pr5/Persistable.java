package pr5;

import java.util.HashMap;

public interface Persistable <T> {
    public abstract T add(T obj, int id);
    public abstract T delete(T id);
    public abstract T get(int id);
    public abstract HashMap<Integer, T> getMap();
    //TODO collection
}
