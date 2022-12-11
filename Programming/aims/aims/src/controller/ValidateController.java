package controller;

import java.io.IOException;
import java.util.HashMap;

public class ValidateController extends BaseController{
	/**
	   * The method validates the info
	   * @param info
	   * @throws InterruptedException
	   * @throws IOException
	   */
	    public static void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
	    	assert(validateName(info.get("name")));
	    	assert(validatePhoneNumber(info.get("phone")));
	    	assert(validateAddress(info.get("address")));
	    	
	    }
	    
	    public static void validateRushDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
	    	validateRushOrder(info.get("address"));
	    }
	    
	    /**
	     * validate a rush address
	     * @param address
	     * @throws InterruptedException
	     * @throws IOException
	     */
	    public static void validateRushOrder(String address) throws InterruptedException, IOException{
	    	assert(validateRushOrderAddress(address));
	    }
	    
	    public static boolean validateRushOrderAddress(String address){
	    	return (address != null) && (address.contains("Ha Noi") || address.contains("Ho Chi Minh"));
	    }
	    
	    /**
	     * validate a phone number
	     * @param phoneNumber
	     * @return
	     */
	    public static boolean validatePhoneNumber(String phoneNumber) {
	    	if((phoneNumber.charAt(0) != '0') || (phoneNumber.length() != 10))
	    		return false;
	    	try {
	    		if(phoneNumber == null)
	    			return false;
	    		for(int i=0; i<phoneNumber.length(); ++i) {
	        		if(!(Character.isDigit(phoneNumber.charAt(i)) || phoneNumber.charAt(i) == ' '))
	        			return false;
	        	}
	    		return true;
	    	}catch(NumberFormatException e) {
	    		return false;
	    	}
	    }
	    
	    /**
	     * validate name
	     * @param name
	     * @return
	     */
	    public static boolean validateName(String name) {
	    	if ((name == null) || (name.length() == 0))
	            return false;
	    	if(!(Character.isLetter(name.charAt(name.length()-1)) && Character.isLetter(name.charAt(0))))
	    		return false;
	    	for(int i=0; i<name.length(); ++i) {
	    		if(!(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' '))
	    			return false;
	    	}
	    	return true;
	    }
	    
	    /**
	     * validate address of delivery
	     * @param address
	     * @return
	     */
	    public static boolean validateAddress(String address) {
	    	if ((address == null) || (address.length() == 0))
	            return false;
	    	for(int i=0; i<address.length(); ++i) {
	    		if(!(Character.isLetterOrDigit(address.charAt(i)) || address.charAt(i) == ' '))
	    			return false;
	    	}
	    	return true;
	    }
}
