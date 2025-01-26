import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    enum Rarity {
        COMMON,
        GREAT,
        RARE,
        EPIC,
        EPIC1,
        EPIC2,
        LEGENDARY
    }

    private String name;
    private Rarity rarity;
    private int upgradeCount = 0;


    public Item (String name, Rarity rarity){
        this.name = name;
        this.rarity = rarity;
    }

    public boolean canUpgrade(List<Item> items) {
        Map<String, Map<Rarity, Integer>> itemCounts = new HashMap<>();

        for (Item item : items) {
            itemCounts
                    .computeIfAbsent(item.name, k -> new HashMap<>())
                    .merge(item.rarity, 1, Integer::sum);
        }

        for (Map<Rarity, Integer> rarityCount : itemCounts.values()) {
            if (rarityCount.getOrDefault(Rarity.COMMON, 0) >= 3 ||
                    rarityCount.getOrDefault(Rarity.GREAT, 0) >= 3 ||
                    rarityCount.getOrDefault(Rarity.RARE, 0) >= 3 ||
                    rarityCount.getOrDefault(Rarity.EPIC, 0) >= 2 ||
                    rarityCount.getOrDefault(Rarity.EPIC1, 0) >= 2 ||
                    rarityCount.getOrDefault(Rarity.EPIC2, 0) >= 3) {
                return true;
            }
        }

        return false;
    }

    public Item upgrade(List<Item> items) {
        if (!canUpgrade(items)) {
            return null;
        }

        Map<Rarity, List<Item>> rarityMap = new HashMap<>();

        for (Item item : items) {
            if (item.name.equals(this.name)) {
                rarityMap.computeIfAbsent(item.rarity, k -> new ArrayList<>()).add(item);
            }
        }

        Rarity nextRarity = getNextRarity(this.rarity);
        int requiredItems = (this.rarity == Rarity.EPIC || this.rarity == Rarity.EPIC1) ? 2 : 3;

        List<Item> candidates = rarityMap.getOrDefault(this.rarity, new ArrayList<>());

        if (candidates.size() < requiredItems) {
            return null;
        }

        for (int i = 0; i < requiredItems; i++) {
            items.remove(candidates.get(i));
        }

        if (nextRarity == Rarity.LEGENDARY) {
            this.upgradeCount = 0;
        }

        return new Item(this.name, nextRarity);
    }

    private Rarity getNextRarity(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> Rarity.GREAT;
            case GREAT -> Rarity.RARE;
            case RARE -> Rarity.EPIC;
            case EPIC -> Rarity.EPIC1;
            case EPIC1 -> Rarity.EPIC2;
            case EPIC2 -> Rarity.LEGENDARY;
            default -> rarity;
        };
    }


    public boolean isSameType(Item other) {
        return this.name.equals(other.name) && this.rarity == other.rarity;
    }


    public String toString(){
        return "Type: " + name  + ", Rarity: " + rarity;
    }

    public String getName(){
        return this.name;
    }

    public Rarity getRarity(){
        return this.rarity;
    }

}
