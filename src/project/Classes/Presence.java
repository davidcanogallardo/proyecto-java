package project.Classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import project.Utils.GenericFormatter;

public class Presence implements Comparable<Presence> {
    private Integer id;
    private LocalDate date;
    private LocalTime enterTime;
    private LocalTime leaveTime;

    public Presence(Integer id, LocalDate date, LocalTime enterTime) {
        this.id = id;
        this.date = date;
        this.enterTime = enterTime;
    }

    public Presence(Integer id, LocalDate date, LocalTime enterTime, LocalTime leaveTime) {
        this.id = id;
        this.date = date;
        this.enterTime = enterTime;
        this.leaveTime = leaveTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(LocalTime enterTime) {
        this.enterTime = enterTime;
    }

    public LocalTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Override
    public String toString() {
        return "Presence [id=" + id + ", date=" + date + ", enterTime=" + GenericFormatter.formatTime(enterTime) + ", leaveTime=" + GenericFormatter.formatTime(leaveTime) + "]";
    }

    // Defino que dos Presence son iguales cuando tienen el mismo id y fecha
    // y no tengan fecha de salida
    @Override
    public boolean equals(Object obj) {
        Presence obj2 = (Presence) obj;
        return this.id == obj2.id && this.date.equals(obj2.date) && this.leaveTime == null;
    }

    // Ordeno numericamente por el id y si tienen id ordeno por fecha y hora de
    // entrada
    @Override
    public int compareTo(Presence o) {
        if (this.id != o.id) {
            return this.id.compareTo(o.id);
        } else {
            LocalDateTime ldt = LocalDateTime.of(this.date, this.enterTime);
            LocalDateTime ldt2 = LocalDateTime.of(o.date, o.enterTime);

            return ldt.compareTo(ldt2);
        }
    }
}
