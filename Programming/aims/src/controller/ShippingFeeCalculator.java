package controller;
import entity.order.Order;

public interface ShippingFeeCalculator {
	public int calculateShippingFee(Order order);
}
