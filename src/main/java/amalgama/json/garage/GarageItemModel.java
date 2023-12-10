package amalgama.json.garage;

import amalgama.network.GarageHandler;
import amalgama.network.managers.GarageManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GarageItemModel {
    public List<GarageModificationModel> modification = new ArrayList<>();
    public String id;
    public String description;
    public String name;
    public int modificationID;
    public int index;
    public int type;
    public int next_price;
    public int next_rank;
    public int price;
    public int rank;
    public int count;
    public int discount;
    public boolean isInventory;
    public boolean multicounted;

    public GarageItemModel copy() {
        GarageItemModel i = new GarageItemModel();
        i.modification = new ArrayList<>(modification);
        i.id = id;
        i.description = description;
        i.name = name;
        i.modificationID = modificationID;
        i.index = index;
        i.type = type;
        i.next_price = next_price;
        i.next_rank = next_rank;
        i.price = price;
        i.rank = rank;
        i.count = count;
        i.discount = discount;
        i.isInventory = isInventory;
        i.multicounted = multicounted;
        return i;
    }
}
