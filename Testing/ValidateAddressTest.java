package test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import controller.PlaceOrderController;

import static org.junit.jupiter.api.Assertions.*;

class ValidateAddressTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception{
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
    		"MainAvenue, true",
    		"172WellStreet, true",
            ", false",
            "new String(), false",
            "So nha 1, true",
            "!!!, false"
    })
    void validateAddress(String address, boolean expected) {
        boolean isValid = placeOrderController.validateAddress(address);

        assertEquals(expected, isValid);
    }
}