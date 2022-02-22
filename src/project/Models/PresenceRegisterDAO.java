package project.Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;

public class PresenceRegisterDAO {
    public HashSet<Presence> hashSet = new HashSet<>();

    public Presence add(Presence obj) {
        if (this.hashSet.contains(obj)) {
            System.out.println("no añadido");
            return null;
        } else {
            System.out.println("añadido");
            this.hashSet.add(obj);
            System.out.println(this.hashSet);
            return obj;
        }
    }

    public void addLeaveTime(int id) {
        LocalDate today = LocalDate.now();
        for (Presence presence : this.hashSet) {
            if (presence.getId() == id && presence.getDate().compareTo(today) == 0) {
                LocalTime now = LocalTime.now(); 
                presence.setLeaveTime(now);
            }
        }
    }

    public void list() {
        System.out.println("addd");
        System.out.println(this.hashSet);
        // for (Presence presence : hashSet) {
        //     System.out.println(presence.toString());
            
        // }
    }
}
