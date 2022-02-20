package project.Models;

import java.util.HashMap;
import java.util.HashSet;

public class PresenceRegisterDAO {
    private HashSet<Presence> hashSet = new HashSet<>();

    public Presence add(Presence obj) {
        if (hashSet.contains()) {
            return null;
        } else {
            hashSet.put(obj.getId(), obj);
            return obj;
        }
    }
}
