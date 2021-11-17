package pr5;

import java.util.Objects;

public class Product {
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
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
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
        if (obj instanceof Product) {
            Product tmp = (Product)obj;
            return this.name.equals(tmp.getName());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Product{" + "idProduct=" + idProduct + ", name=" + name + ", price=" + price + ", stock=" + stock + '}';
    }
    
    //Getters
    public Integer getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    //Setters
    public void setIdProduct(Integer idProduct) {
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
