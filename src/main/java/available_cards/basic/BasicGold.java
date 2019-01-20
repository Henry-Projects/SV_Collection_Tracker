package available_cards.basic;

public enum BasicGold {

    BasicGold ("test-1");

    private final String name;

    private BasicGold(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

}
