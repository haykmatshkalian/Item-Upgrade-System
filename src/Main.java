import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Inventory System ===");
            System.out.println("1. Display Inventory");
            System.out.println("2. Add Item");
            System.out.println("3. Upgrade Item");
            System.out.println("4. Save Inventory");
            System.out.println("5. Load Inventory");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    inventory.displayInventory();
                    break;
                case 2:
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    Item.Rarity randomizedRarity = generateRandomRarity();
                    inventory.addItem(new Item(name, randomizedRarity));
                    System.out.println("Added item: " + name + " with rarity: " + randomizedRarity);
                    break;
                case 3:
                    System.out.print("Enter the name of the item to upgrade: ");
                    String itemName = scanner.nextLine();
                    if (!inventory.upgradeItem(itemName)) {
                        System.out.println("Upgrade failed. Check item availability or requirements.");
                    }
                    break;
                case 4:
                    System.out.print("Enter file name to save: ");
                    String saveFileName = scanner.nextLine();
                    inventory.saveToFile(saveFileName);
                    break;
                case 5:
                    System.out.print("Enter file name to load: ");
                    String loadFileName = scanner.nextLine();
                    inventory.loadFromFile(loadFileName);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }


    private static Item.Rarity generateRandomRarity() {
        double random = Math.random();
        if (random < 0.5) {
            return Item.Rarity.COMMON;
        } else if (random < 0.75) {
            return Item.Rarity.GREAT;
        } else if (random < 0.9) {
            return Item.Rarity.RARE;
        } else if (random < 0.98) {
            return Item.Rarity.EPIC;
        } else {
            return Item.Rarity.LEGENDARY;
        }
    }
}
