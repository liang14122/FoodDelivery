package sg.edu.rp.c346.fooddelivery;

/**
 * Created by 16004118 on 13/11/2017.
 */

public class OrderItem {
    private String name;
    private Integer quantity;
    private Double price;

    public OrderItem(String name, Integer quantity, Double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
