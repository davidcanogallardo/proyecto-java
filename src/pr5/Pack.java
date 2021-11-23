package pr5;

import java.util.ArrayList;

public final class Pack extends Product {

    private ArrayList<Integer> idProdList;
    private int discount;

    public Pack(ArrayList<Integer> idProdList, int discount, Integer idProduct, String name, Integer price) {
        super(idProduct, name, price);
        this.idProdList = idProdList;
        this.discount = discount;
    }

    public ArrayList<Integer> getIdProdList() {
        return idProdList;
    }

    public void setIdProdList(ArrayList<Integer> idProdList) {
        this.idProdList = idProdList;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
    
    public boolean deleteProduct(Integer id) {
        for (Integer i : this.idProdList) {
            if (i == id) {
                this.idProdList.remove(id);
                return true;
            }
        }
        return false;
    }
    
    public boolean addProduct(Integer id) {
        for (Integer i : this.idProdList) {
            if (i == id) {
                return false;
            }
        }
        this.idProdList.add(id);
        return true;
    }

    @Override
    public String toString() {
        String str = super.toString() + "\n [\n";
        for (Integer i : this.idProdList) {
            str += "   " + i + ",\n";
        }
        str += "]";
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pack pack) {
            return this.idProdList.equals(pack.getIdProdList());
        } else {
            return false;
        }

    } 
}
