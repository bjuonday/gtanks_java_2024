package amalgama.json.garage;

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
}
