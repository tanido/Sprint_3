import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositiveLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;


    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }


    @Test
    @DisplayName("Логин курьера с валидными данными")
    public void positiveLoginTest() {
        ValidatableResponse response = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = response.extract().path( "id");
        int statusCode = response.extract().statusCode();

        assertEquals("Отличный от ожидаемого код ответа", statusCode, 200);
        assertNotNull("id не возвращается", courierId);
    }
}
