import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(items);
            System.out.println("Inventory saved successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            items = (List<Item>) ois.readObject();
            System.out.println("Inventory loaded successfully from " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    public void addItem(Item item){
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the inventory.");
        }
        items.add(item);
    }

    boolean removeItem(Item item){
        if(items.contains(item)){
            items.remove(item);
            return true;
        }
        return false;
    }

    public boolean upgradeItem(String itemName) {
        List<Item> matchingItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                matchingItems.add(item);
            }
        }

        if (matchingItems.isEmpty()) {
            System.out.println("No items found with the name: " + itemName);
            return false;
        }

        Item targetItem = matchingItems.get(0);
        if (!targetItem.canUpgrade(items)) {
            System.out.println("Not enough items of the same rarity for upgrading: " + itemName);
            return false;
        }

        Item upgradedItem = targetItem.upgrade(items);
        if (upgradedItem != null) {
            items.add(upgradedItem);
            System.out.println(itemName + " upgraded successfully to " + upgradedItem);
            return true;
        }

        System.out.println("Upgrade failed for: " + itemName + ". Please check the requirements.");
        return false;
    }

    public void displayInventory() {
        if (items.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        Map<Item.Rarity, List<Item>> groupedByRarity = new HashMap<>();
        for (Item item : items) {
            groupedByRarity
                    .computeIfAbsent(item.getRarity(), k -> new ArrayList<>())
                    .add(item);
        }

        System.out.println("=== Inventory ===");
        for (Item.Rarity rarity : Item.Rarity.values()) {
            List<Item> rarityGroup = groupedByRarity.getOrDefault(rarity, new ArrayList<>());
            if (!rarityGroup.isEmpty()) {
                System.out.println(rarity + ":");
                for (Item item : rarityGroup) {
                    System.out.println("  " + item);
                }
            }
        }
    }

    public List<Item> getItemsByNameAndRarity(String name, Item.Rarity rarity) {
        List<Item> matchingItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName().equals(name) && item.getRarity() == rarity) {
                matchingItems.add(item);
            }
        }
        return matchingItems;
    }

    public boolean hasEnoughItemsForUpgrade(String name, Item.Rarity rarity){
        Item item = new Item(name, rarity);
        return item.canUpgrade(items);
    }
}
