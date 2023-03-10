package shoppingCart1;

import java.io.FileWriter;
import java.io.IOException;

public class FileHandling {
    private String path;
    private int cartsCount;
    private String nameChosen;
    private ReaderWriter readerWriter;
    private FileWriter fileWriter;
    private StringBuilder userItems = new StringBuilder();
    private boolean madeChanges = false;

    public FileHandling(String path) {
        this.path = path;
    }

    public void run() throws IOException {
        this.readerWriter = new ReaderWriter(this.path);
        cartsCount = readerWriter.getNumberOfCarts(this.path);
        System.out.printf("There are %d carts in the shopping directory.\n", cartsCount);
    }

    public void exit() {
        System.out.println("Thank you for using Shopping Cart On-The-Go!\n");
        System.exit(1);
    }

    public void load(String name) throws IOException {
        this.readerWriter = new ReaderWriter(this.path);
        this.nameChosen = name;
        if (readerWriter.checkExistence(name)) {
            System.out.printf("%s's shopping cart loaded.\n",
                    (name.substring(0, 1).toUpperCase() + name.substring(1)));
        } else {
            readerWriter.createFile(name + ".cart");
            System.out.printf("%s's shopping cart loaded.\n",
                    (name.substring(0, 1).toUpperCase() + name.substring(1)));
        }

    }

    public void list() throws IOException {
        this.readerWriter = new ReaderWriter(this.path);
        readerWriter.readFile(nameChosen);
        String line;
        int numOfItemsRemoved = 0;
        boolean itemRemoved = false;

        if (!madeChanges) {
            // READS ALL THE LINE IN THE FILE
            while (null != (line = readerWriter.bufferedFile())) {
                String[] lineArray = line.trim().split(" ");
                for (int i = 0; i < lineArray.length; i++) {
                    this.userItems.append(String.format("%s ", lineArray[i]));
                    System.out.printf("%d. %s\n", i + 1, lineArray[i]);
                }
            }
            readerWriter.closeBuffer();
            readerWriter.closeReader();
        } else {
            String[] lineArray = userItems.toString().trim().split(" ");
            for (int i = 0; i < lineArray.length; i++) {
                if (lineArray[i].isEmpty()) {
                    itemRemoved = true;
                    numOfItemsRemoved++;
                    continue;
                }
                if (!itemRemoved) {
                    System.out.printf("%d. %s\n", i + 1, lineArray[i]);
                } else {
                    System.out.printf("%d. %s\n", i + 1 - numOfItemsRemoved, lineArray[i]);
                }
            }
        }
    }

    public void add(String items) throws IOException {
        String outputString = "";
        String[] itemsAddedArray = items.trim().split(" ");
        for (int i = 0; i < itemsAddedArray.length; i++) {
            for (int j = 0; j < userItems.length(); j++) {
                if (userItems.indexOf(itemsAddedArray[i]) == -1) {
                    this.userItems.append(String.format("%s ", itemsAddedArray[i]));
                    outputString += itemsAddedArray[i] + " ";
                    madeChanges = true;
                }
            }
        }
        if (outputString.equalsIgnoreCase("")) {
            System.out.println("Nothing new added to the cart.");
        } else {
            System.out.println(outputString += "added to the cart.");
        }
    }

    public void delete(int index) throws IOException {
        String userItemsString = "";
        String itemRemoved = "";
        String[] userItemsArr = userItems.toString().split(" ");
        for (int i = 0; i < userItemsArr.length; i++) {
            if (index > userItemsArr.length || index < 0) {
                System.out.println("Incorrect item index.");
            }
            if (i == index) {
                System.out.printf("Removed %s from cart.\n", userItemsArr[i]);
                itemRemoved = userItemsArr[i];
                continue;
            }
            userItemsString += userItemsArr[i] + " ";
        }
        this.userItems.replace(0, (userItemsString.length() + itemRemoved.length()), userItemsString);
        this.madeChanges = true;
    }

    public void save() throws IOException {
        fileWriter = readerWriter.fileWriter(nameChosen);
        fileWriter.write(userItems.toString());
        fileWriter.flush();
        fileWriter.close();
        System.out.println("Cart saved successfully.");
    }

}