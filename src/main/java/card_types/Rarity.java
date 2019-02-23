package card_types;

public enum Rarity {

    BRONZE(10,50, 30), SILVER(50,200, 120), GOLD(250,800, 600), LEGENDARY(1000,3500, 2500);

    private int liquefy_value;
    private int create_value;
    private int liquefyAnimated_value;

    Rarity(int normal_liquefy, int normal_create, int animated_liquefy){
        liquefy_value = normal_liquefy;
        create_value = normal_create;
        liquefyAnimated_value = animated_liquefy;
    }

    public int getLiquefy_value() {
        return liquefy_value;
    }
    public int getCreate_value() {
        return create_value;
    }
    public int getLiquefyAnimated_value() { return liquefyAnimated_value;}

}
