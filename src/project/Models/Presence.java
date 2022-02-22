package project.Models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Presence {
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
        return "Presence [date=" + date + ", enterTime=" + enterTime + ", id=" + id + ", leaveTime=" + leaveTime + "]";
    }

    @Override
    public boolean equals(Object obj) {
        Presence obj2 = (Presence) obj;
        return this.id == obj2.id && this.date.equals(obj2.date);
    }
}