package test.controller;

import static org.junit.jupiter.api.Assertions.*;
import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidatePhoneNumberTest {
	private PlaceOrderController placeOrderController;
	
	@BeforeEach
	void setUp() throws Exception {
		this.placeOrderController = new PlaceOrderController();
	}

	
	@ParameterizedTest
    @CsvSource({
    		"null, false",
    		"new String(), false",
            "01234, false",
            "01233456789, false",
            "01a3456789, false",
            "9876543210, false",
            "0123456789, true"
    })
	
	void validatePhoneNumber(String phone, boolean expected) {
        boolean isValid = placeOrderController.validatePhoneNumber(phone);
        assertEquals(expected, isValid);
    }

}
