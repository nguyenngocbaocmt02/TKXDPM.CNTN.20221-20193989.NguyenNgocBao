package test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import controller.PlaceOrderController;

import static org.junit.jupiter.api.Assertions.*;

class ValidateNameTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception{
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
    		", false",
    		"new String(), false",
            "abc@12, false",
            "Vuong!DinhAn, false",
            "VuongDinhAn1, false",
            "VuongDinhAn, true",
            "Vuong Dinh An, true",
//            "VuongDinhAn , false",
//            " Vuong Dinh An, false" 

    })
    void validateName(String name, boolean expected) {
        boolean isValid = placeOrderController.validateName(name);
        assertEquals(expected, isValid);
    }
}