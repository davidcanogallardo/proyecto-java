package project.Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class PresenceRegisterDAO {
    public TreeSet<Presence> hashSet = new TreeSet<>();

    public Presence add(Presence obj) {
        for (Presence presence : hashSet) {
            if (presence.equals(obj)) {
                System.out.println("no añadido");
                return null;
            }
        }
        // if (this.hashSet.contains(obj)) {
        // System.out.println("no añadido");
        // return null;
        // } else {
        // System.out.println("añadido");
        // this.hashSet.add(obj);
        // System.out.println(this.hashSet);
        // return obj;
        // }
        System.out.println("añadido");
        this.hashSet.add(obj);
        return obj;
    }

    public void addLeaveTime(int id) {
        LocalDate today = LocalDate.now();
        for (Presence presence : this.hashSet) {
            if (presence.getId() == id && presence.getDate().compareTo(today) == 0 && presence.getLeaveTime() == null) {
                LocalTime now = LocalTime.now();
                presence.setLeaveTime(now);
            }
        }
    }

    public void list() {
        for (Presence presence : hashSet) {
        System.out.println(presence.toString());

        }
    }
}
