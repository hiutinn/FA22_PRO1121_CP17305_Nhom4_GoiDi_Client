package hieuntph22081.fpoly.goidiclient.model;

public class OrderDish {
    private Dish dish;
    private int quantity;

    public OrderDish() {
    }

    public OrderDish(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
