package amalgama.lobby;

import amalgama.Global;
import amalgama.database.User;
import amalgama.database.dao.UserItemDAO;
import amalgama.json.garage.SendGarageModel;

import java.util.List;

public class GarageManager {
    public static SendGarageModel getGarage(User user) {
        SendGarageModel sendGarageModel = new SendGarageModel();
        List<String> garageItems = UserItemDAO.getUserItemIds(user);
        for (var garageItem : garageItems) {
            for (var item : Global.garageItems) {
                if (item.id.equals(garageItem.substring(0, garageItem.length() - 3))) {
                    sendGarageModel.garage.add(item);
                    break;
                }
                else if (item.rank < 0 || item.price < 0)
                    continue;

                if (!sendGarageModel.market.contains(item))
                    sendGarageModel.market.add(item);
            }
        }
        sendGarageModel.market.removeAll(sendGarageModel.garage);
        return sendGarageModel;
    }
}
