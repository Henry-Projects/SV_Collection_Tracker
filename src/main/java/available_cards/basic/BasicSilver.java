package available_cards.basic;

import card_types.Rarity;

public enum BasicSilver {

    silver1,
    silver2,
    silver3,
    silver4;

    public String getRarity(){
        return String.valueOf(Rarity.NORMAL_SILVER);
    }

    public int getLiquefy_values(){
        return Rarity.NORMAL_SILVER.getLiquefy_value();
    }

    public int getCreate_values(){
        return Rarity.NORMAL_SILVER.getCreate_value();
    }

}
