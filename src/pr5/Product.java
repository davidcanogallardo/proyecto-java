package pr5;

import java.util.Objects;

public class Product implements Identificable{
    private Integer idProduct;
    private String name;
    private Integer price;
    private Integer stock;
    
    public Product(Integer idProduct, String name, Integer price) {
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
    }
    
    public Product(Integer idProduct, String name, Integer price, Integer stock) {
        this(idProduct, name, price);
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        Integer hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product prod) {
            return this.name.equals(prod.getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Product [" + "idProduct=" + idProduct + ", name=" + name + ", price=" + price + ", stock=" + stock + ']';
    }
    
    //Getters
    public Integer getId() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    //Setters
    public void setId(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}