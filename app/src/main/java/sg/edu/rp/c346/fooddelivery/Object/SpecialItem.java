package sg.edu.rp.c346.fooddelivery.Object;

/**
 * Created by 16004118 on 22/10/2017.
 */

public class SpecialItem {
    private String foodName;
    private int quantity;

    public SpecialItem(String foodName, int quantity){
        this.foodName = foodName;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
