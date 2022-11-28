import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import controller.PlaceOrderController;

import static org.junit.jupiter.api.Assertions.*;

class ValidateRushOrderAddress{

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception{
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
    		", false",
    		"new String(), false",
            "MainStreet@12, false",
            "MainStreet1, false",
            "12 Bro Avenue, false",
            "12 Ha Noi, true",
            "12 Duong Le Thanh Tong Thanh Pho Ho Chi Minh, true",

    })
    void validateRushOrderAddress(String address, boolean expected) {
        boolean isValid = placeOrderController.validateRushOrderAddress(address);
        assertEquals(expected, isValid);
    }
}