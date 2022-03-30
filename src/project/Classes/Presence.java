package project.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import project.utils.GenericFormatter;

public class Presence implements Comparable<Presence>, Serializable {
    private Integer id;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;

    public LocalDateTime getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(LocalDateTime enterTime) {
        this.enterTime = enterTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Presence(Integer id, LocalDateTime enterTime) {
        this.id = id;
        this.enterTime = enterTime;
    }

    public Presence(Integer id, LocalDateTime enterTime, LocalDateTime leaveTime) {
        this.id = id;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Presence [enterTime=" + GenericFormatter.formatDateTime(enterTime) + ", id=" + id + ", leaveTime=" + GenericFormatter.formatDateTime(leaveTime) + "]";
    }

    // Defino que dos Presence son iguales cuando tienen el mismo id y fecha
    // y no tengan fecha de salida
    @Override
    public boolean equals(Object obj) {
        Presence obj2 = (Presence) obj;
        return this.id == obj2.id && this.leaveTime == null && obj2.leaveTime == null;
    }

    // Ordeno numericamente por el id y si tienen id ordeno por fecha y hora de
    // entrada
    @Override
    public int compareTo(Presence o) {
        if (this.id != o.id) {
            return this.id.compareTo(o.id);
        } else {
            return this.enterTime.compareTo(o.enterTime);
        }
    }
}
