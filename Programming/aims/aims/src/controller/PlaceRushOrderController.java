package controller;

import java.io.IOException;
import java.util.HashMap;

public class PlaceRushOrderController extends PlaceOrderController{
	@Override
	public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        ValidateController.validateDeliveryInfo(info);
        if(this.isRush) {
        	System.out.println("Placing Rush Order");
        	ValidateController.validateRushOrderAddress((String) info.get("address"));
        }
    }
}
