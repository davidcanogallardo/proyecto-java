package pr5;

import java.util.HashMap;

public class Manager <T> {
    private HashMap<Integer, T> hashMap = new HashMap<>();

    public T add(T obj, int id) {
        if (hashMap.containsKey(id)) {
            return null;
        } else {
            hashMap.put(id, obj);
            return (T)obj;
        }
    }

    public T delete(T id) {
        if (hashMap.containsKey(id)) {
            T obj = hashMap.get(id);
            hashMap.remove(id);
            return obj;
        } else {
            return null;
        }
    }

    public T get(int id) {
        if (hashMap.containsKey(id)) {
            return hashMap.get(id);
        } else {
            return null;
        }
    }

    public HashMap<Integer, T> getMap() {
        return new HashMap<>(hashMap);
    }

    public void modifyClient(int id, T obj) {
        System.out.println("w");
    }

    public boolean objExists(int id) {
        return hashMap.containsKey(id);
    }

}