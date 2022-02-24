package project.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import project.Exceptions.StockInsuficientException;

public class Product implements Identificable, Serializable, Comparable<Product>, Cloneable {
    private Integer id;
    private String name;
    private double price;
    private Integer stock;
    private LocalDate startCatalog;
    private LocalDate endCatalog;

    public Product(Integer id, String name, double price, LocalDate startCatalog,
            LocalDate endCatalog) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.startCatalog = startCatalog;
        this.endCatalog = endCatalog;
    }

    public Product(Integer id, String name, double price, Integer stock, LocalDate startCatalog,
            LocalDate endCatalog) {
        this(id, name, price, startCatalog, endCatalog);
        this.stock = stock;

    }

    public LocalDate getStartCatalog() {
        return startCatalog;
    }

    public void setStartCatalog(LocalDate startCatalog) {
        this.startCatalog = startCatalog;
    }

    public LocalDate getEndCatalog() {
        return endCatalog;
    }

    public void setEndCatalog(LocalDate endCatalog) {
        this.endCatalog = endCatalog;
    }

    @Override
    public int hashCode() {
        Integer hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product prod = (Product) obj;
            return this.name.equals(prod.getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price
                + ", startCatalog=" + startCatalog + ", endCatalog=" + endCatalog + ", stock=" + stock + "]";
    }

    public void putStock(int num) {
        this.stock += num;
    }

    public void takeStock(int num) throws StockInsuficientException {
        if (num > this.stock) {
            throw new StockInsuficientException();
        } else {
            this.stock -= num;
        }
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setId(Integer idProduct) {
        this.id = idProduct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public int compareTo(Product o) {
        if (this.id > o.id) {
            return 1;
        } else if (this.id < o.id) {
            return -1;
        } else {
            return 0;
        }
    }

    public Object clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Product(this.id, this.name, this.price, this.stock, this.startCatalog, this.endCatalog);
        }
    }
}