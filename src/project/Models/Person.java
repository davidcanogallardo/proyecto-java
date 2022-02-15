package project.Models;

import static java.lang.Math.abs;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Person implements Identificable, Serializable {
    private Integer idPersona;
    private String dni;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String email;
    private String phoneNumber;
    private Address fullAddress;
    private static int totalPeople;

    protected Person(Integer idPersona, String dni, String name, String surname, Address fullAddress) {
        this.idPersona = idPersona;
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.fullAddress = fullAddress;

        totalPeople++;
    }

    protected Person(Integer idPersona, String dni, String name, String surname, LocalDate birthdate, String email,
            String phoneNumber, Address fullAddress) {
        this(idPersona, dni, name, surname, fullAddress);
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Person [" + "idPersona=" + idPersona + ", dni=" + dni + ", name=" + name + ", surname=" + surname
                + ", birthdate=" + birthdate + ", email=" + email + ", phoneNumber=" + phoneNumber + ", fullAddress="
                + fullAddress + ']';
    }

    public Integer getAge() {
        return (int) ChronoUnit.YEARS.between(this.getBirthdate(), LocalDate.now());
    }

    public static long ageDifference(Person p1, Person p2) {
        return abs(p1.getAge() - p2.getAge());
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullAddress(Address fullAddress) {
        this.fullAddress = fullAddress;
    }

    public static void setTotalPeople(Integer totalPeople) {
        Person.totalPeople = totalPeople;
    }

    public Integer getId() {
        return idPersona;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getFullAddress() {
        return fullAddress;
    }

    public static Integer getTotalPeople() {
        return totalPeople;
    }
}