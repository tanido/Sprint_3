import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateCourierWithoutFieldsTest {
    private CourierClient courierClient;
    private final Courier courier;
    private final int expectedStatusCode;
    private final String expectedErrorMessage;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        int statusCode = courierClient.loginCourier(new CourierCredentials(courier.login, courier.password)).extract().statusCode();
        if (statusCode == 201) {
            int courierId = courierClient.loginCourier(new CourierCredentials(courier.login, courier.password)).extract().path("id");
            courierClient.deleteCourier(courierId);
        }
    }

    public CreateCourierWithoutFieldsTest(Courier courier, int expectedStatusCode, String expectedErrorMessage) {
        this.courier = courier;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierData() {
        return new Object[][] {
                {Courier.createCourierWithLoginOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.createCourierWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.createCourierWithNameOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.createCourierWithoutName(), 201, null}
        };
    }

    @Test
    @DisplayName("Создание курьера не со всеми полями")
    public void createCourierWithoutFieldsTest() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int actualStatusCode = response.extract().statusCode();
        String actualErrorMessage = response.extract().path("message");

        assertEquals("Отличный от ожидаемого код ответа", expectedStatusCode, actualStatusCode);
        assertEquals("Неверное сообщение об ошибке", expectedErrorMessage, actualErrorMessage);
    }
}
