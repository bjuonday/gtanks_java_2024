package amalgama.network.managers;

import amalgama.Global;
import amalgama.database.User;
import amalgama.database.UserItem;
import amalgama.database.UserMount;
import amalgama.database.dao.UserDAO;
import amalgama.database.dao.UserItemDAO;
import amalgama.database.dao.UserMountDAO;
import amalgama.json.garage.GarageItemModel;
import amalgama.json.garage.GarageModificationModel;
import amalgama.json.garage.SendGarageModel;
import amalgama.utils.RankUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GarageManager {
    public static SendGarageModel getGarage(User user) throws IOException, ClassNotFoundException {
        SendGarageModel sendGarageModel = new SendGarageModel();
        List<String> garageItems = UserItemDAO.getUserItemIds(user);

        for (var item : Global.garageItems) {
            boolean has = false;
            for (var garageItem : garageItems) {
                if (item.id.equals(garageItem.substring(0, garageItem.length() - 3))) {
                    GarageItemModel itemModel = item.copy();
                    itemModel.modificationID = Character.getNumericValue(garageItem.charAt(garageItem.length() - 1));
                    itemModel.next_price = getNextPrice(itemModel.modificationID, itemModel.modification);
                    sendGarageModel.garage.add(itemModel);
                    has = true;
                    break;
                }
            }
            if (has || (item.rank < 0 || item.price < 0))
                continue;
            sendGarageModel.market.add(item);
        }

        return sendGarageModel;
    }

    private static int getNextPrice(int currentMod, List<GarageModificationModel> modifications) {
        if (modifications.size() > 3) {
            return currentMod < 3 ? modifications.get(currentMod + 1).price : 0;
        }
        return 0;
    }

    public static int buyItem(User user, String itemId, int count) {
        int balance = user.getBalance();
        int rank = RankUtils.getRankFromScore(user.getScore());
        for (var item : Global.garageItems) {
            if (item.id.equals(itemId.substring(0, itemId.length() - 3))) {
                if (item.rank > rank || (item.price * count) > balance)
                    return -1;
                addItem(user, itemId, count);
                user.setBalance(balance - item.price);
                UserDAO.updateUser(user);
                return user.getBalance();
            }
        }
        return -2;
    }

    private static void addItem(User user, String itemId, int count) {
        List<UserItem> items = UserItemDAO.getUserItems(user);
        for (var item : items) {
            if (item.getItemId().equals(itemId)) {
                item.setCount(item.getCount() + count);
                UserItemDAO.updateItem(item);
                return;
            }
        }
        UserItem item = new UserItem();
        item.setItemId(itemId);
        item.setCount(count);
        item.setUser(user);
        UserItemDAO.addItem(item);
    }

    private static int getItemType(String itemId) {
        for (var i : Global.garageItems)
            if (i.id.equals(itemId.substring(0, itemId.length() - 3)))
                return i.type; return 0;
    }

    public static boolean mountItem(User user, String itemId) {
        UserItem item = UserItemDAO.getUserItem(user, itemId);
        if (item != null) {
            int type = getItemType(itemId);
            UserMount mount = UserMountDAO.getMount(user);
            assert mount != null : "Mount not found.";
            switch (type) {
                case 1 -> mount.setWeaponId(itemId);
                case 2 -> mount.setArmorId(itemId);
                case 3 -> mount.setColorId(itemId);
                default -> {
                    return false;
                }
            }
            UserMountDAO.updateMount(mount);
            return true;
        }
        return false;
    }

    public static int upgradeItem(User user, String itemId) {
        UserItem item = UserItemDAO.getUserItem(user, itemId);
        if (item != null) {
            int rank = RankUtils.getRankFromScore(user.getScore());
            int balance = user.getBalance();
            GarageItemModel I = getUpgradeModel(itemId);
            if (I.modificationID >= 3)
                return -1;

            if (I.rank > rank || I.price > balance)
                return -1;

            updateItem(user, itemId);
            user.setBalance(balance - I.price);
            UserDAO.updateUser(user);
            return user.getBalance();
        }
        return -2;
    }

    private static GarageItemModel getUpgradeModel(String itemId) {
        int mod = Character.getNumericValue(itemId.charAt(itemId.length() - 1));
        if (mod < 3) {
            for (var i : Global.garageItems) {
                if (i.id.equals(itemId.substring(0, itemId.length() - 3))) {
                    GarageItemModel newItem = new GarageItemModel();
                    GarageModificationModel modification = i.modification.get(mod + (i.type > 2 ? 0 : 1));
                    newItem.id = i.id;
                    newItem.description = i.description;
                    newItem.name = i.name;
                    newItem.modificationID = mod;
                    newItem.index = i.index;
                    newItem.count = i.count;
                    newItem.discount = i.discount;
                    newItem.next_price = modification.price;
                    newItem.price = modification.price;
                    newItem.next_rank = modification.rank;
                    newItem.rank = modification.rank;
                    newItem.type = i.type;
                    newItem.isInventory = i.isInventory;
                    newItem.multicounted = i.multicounted;
                    newItem.modification = new ArrayList<>(i.modification);
                    return newItem;
                }
            }
        }
        return null;
    }

    private static void updateItem(User user, String itemId) {
        int mod = Character.getNumericValue(itemId.charAt(itemId.length() - 1));
        if (mod < 3) {
            String newId = itemId.substring(0, itemId.length() - 1).concat(String.valueOf(mod + 1));
            UserItem item = UserItemDAO.getUserItem(user, itemId);
            assert item != null : "item null";
            item.setItemId(newId);
            UserItemDAO.updateItem(item);
        }
    }
}
