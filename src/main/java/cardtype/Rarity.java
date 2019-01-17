package cardtype;


public enum Rarity {

    NORMAL_BRONZE(10,50), NORMAL_SILVER(50,200), NORMAL_GOLD(250,800), NORMAL_LEGENDARY(1000,3500),
    ANIMATED_BRONZE(30,-1), ANIMATED_SILVER(120,-1), ANIMATED_GOLD(600,-1), ANIMATED_LEGENDARY(2500,-1);

    private int liquefy_value;
    private int create_value;

    Rarity(int a, int b){
        liquefy_value = a;
        create_value = b;
    }

    int getLiquefy_value() {
        return liquefy_value;
    }
    int getCreate_value() {
        return create_value;
    }

}
