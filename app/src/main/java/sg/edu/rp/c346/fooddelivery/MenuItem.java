package sg.edu.rp.c346.fooddelivery;

/**
 * Created by 16004118 on 8/11/2017.
 */

public class MenuItem {
    private String foodName;
    private String description;
    private double price;
    private String category;
    private int quantity;
    private int mostSeller;

    public MenuItem(String foodName, String description, double price, String category, int quantity, int mostSeller) {
        this.foodName = foodName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.mostSeller = mostSeller;
//        BuildConfig.DEBUG;
    }

    public int getMostSeller() {
        return mostSeller;
    }

    public void setMostSeller(int mostSeller) {
        this.mostSeller = mostSeller;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
