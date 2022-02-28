package project.DAOs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeSet;

import project.Classes.Presence;

public class PresenceRegisterDAO {
    public TreeSet<Presence> hashSet = new TreeSet<>();

    public Presence add(Presence obj) {
        for (Presence presence : hashSet) {
            if (presence.equals(obj)) {
                return null;
            }
        }
        this.hashSet.add(obj);
        return obj;
    }

    public boolean addLeaveTime(int id) {
        LocalDate today = LocalDate.now();
        for (Presence presence : this.hashSet) {
            if (presence.getId() == id && presence.getDate().compareTo(today) == 0 && presence.getLeaveTime() == null) {
                LocalTime now = LocalTime.now();
                presence.setLeaveTime(now);
                return true;
            }
        }
        return false;
    }

    public void list() {
        for (Presence presence : hashSet) {
        System.out.println(presence.toString());

        }
    }
}
