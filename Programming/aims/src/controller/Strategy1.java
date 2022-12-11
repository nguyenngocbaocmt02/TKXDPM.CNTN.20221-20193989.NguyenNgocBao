package controller;

import java.util.Random;

import entity.order.Order;

public class Strategy1 implements ShippingFeeCalculator{
	@Override
	public int calculateShippingFee(Order order){
        Random rand = new Random();
        int fees;
        fees = (int)( ( (rand.nextFloat()*50)/100 ) * order.getAmount() );
        return fees;
    }
}
