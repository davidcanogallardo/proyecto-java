package pr5;

import java.util.HashMap;

public class Manager<T extends Identificable> {
    private HashMap<Integer, T> hashMap = new HashMap<>();

    public T add(T obj) {
        if (hashMap.containsKey(obj.getId())) {
            return null;
        } else {
            hashMap.put(obj.getId(), obj);
            return obj;
        }
    }

    public T delete(int id) {
        if (hashMap.containsKey(id)) {
            T obj = hashMap.get(id);
            hashMap.remove(id);
            return obj;
        } else {
            return null;
        }
    }

    public T get(Integer id) {
        if (hashMap.containsKey(id)) {
            return hashMap.get(id);
        } else {
            return null;
        }
    }

    public HashMap<Integer, T> getMap() {
        return new HashMap<>(hashMap);
    }

    public void modify(T obj) {
        hashMap.replace(obj.getId(), get(obj.getId()), obj);
    }

    public boolean objExists(int id) {
        return hashMap.containsKey(id);
    }

}