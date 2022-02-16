package project.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public final class Pack extends Product implements Serializable {
    // private ArrayList<Integer> productList;
    private TreeSet<Product> productList;
    private int discount;

    // public Pack(ArrayList<Integer> idProdList, int discount, Integer idProduct, String name, double price) {
    public Pack(TreeSet<Product> productList, int discount, Integer idProduct, String name, double price) {
        super(idProduct, name, price);
        this.productList = productList;
        this.discount = discount;
    }

    public TreeSet<Product> getProdList() {
        return productList;
    }

    public void setProdList(TreeSet<Product> idProdList) {
        this.productList = idProdList;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public boolean deleteProduct(Product id) {
        for (Product i : this.productList) {
            if (i == id) {
                this.productList.remove(id);
                return true;
            }
        }
        return false;
    }
    
    public boolean addProduct(Product product) {
        for (Product i : this.productList) {
            if (i == product) {
                return false;
            }
        }
        this.productList.add(product);
        return true;
    }

    @Override
    public String toString() {
        String str = super.toString() + "\nProducts [\n";
        for (Product i : this.productList) {
            str += " " + i + ",\n";
        }
        str += "]";
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pack pack) {
            return this.productList.equals(pack.getProdList());
        } else {
            return false;
        }

    } 
}
