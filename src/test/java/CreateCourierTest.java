import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CreateCourierTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }


    @Test
    @DisplayName("Создание курьера")
    public void courierSuccessfullyCreatedTest() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path( "id");
        boolean isCourierCreated = response.extract().path("ok");

        assertEquals("Отличный от ожидаемого код ответа", 201, statusCode);
        assertTrue("Курьер не создан", isCourierCreated);
    }

    @Test
    @DisplayName("Создание идентичного курьера")
    @Description("Нельзя создать идентичного курьера")
    public void cannotCreateSameCourierTest() {
        courierClient.createCourier(courier);
        ValidatableResponse login = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = login.extract().path( "id");
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isCourierWithSameLoginNotCreated = response.extract().path("message").equals("Этот логин уже используется. Попробуйте другой.");

        assertEquals("Отличный от ожидаемого код ответа", 409, statusCode);
        assertTrue("Создан курьер с существующим логином", isCourierWithSameLoginNotCreated);
    }
}
