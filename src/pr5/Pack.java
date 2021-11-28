package pr5;

import java.util.ArrayList;

public final class Pack extends Product {
    private ArrayList<Integer> productList;
    private int discount;

    public Pack(ArrayList<Integer> idProdList, int discount, Integer idProduct, String name, double price) {
        super(idProduct, name, price);
        this.productList = idProdList;
        this.discount = discount;
    }

    public ArrayList<Integer> getIdProdList() {
        return productList;
    }

    public void setIdProdList(ArrayList<Integer> idProdList) {
        this.productList = idProdList;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public boolean deleteProduct(Integer id) {
        for (Integer i : this.productList) {
            if (i == id) {
                this.productList.remove(id);
                return true;
            }
        }
        return false;
    }
    
    public boolean addProduct(Integer id) {
        for (Integer i : this.productList) {
            if (i == id) {
                return false;
            }
        }
        this.productList.add(id);
        return true;
    }

    @Override
    public String toString() {
        String str = super.toString() + "\nProducts [\n";
        for (Integer i : this.productList) {
            str += " " + i + ",\n";
        }
        str += "]";
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pack pack) {
            return this.productList.equals(pack.getIdProdList());
        } else {
            return false;
        }

    } 
}
