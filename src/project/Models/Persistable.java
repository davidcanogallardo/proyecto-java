package project.Models;

import java.util.HashMap;

public interface Persistable <T> {
    public abstract T add(T obj);
    public abstract T delete(T id);
    public abstract T get(Integer id);
    public abstract HashMap<Integer, T> getMap();
}
