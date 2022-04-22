import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class NegativeLoginCourierTest {

    private static Courier courier = Courier.getRandom();

    private CourierClient courierClient;
    private int courierId;
    private final CourierCredentials courierCredentials;
    private final int expectedStatusCode;
    private final String expectedErrorMessage;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }


    public NegativeLoginCourierTest(CourierCredentials courierCredentials, int expectedStatusCode, String expectedErrorMessage) {
        this.courierCredentials = courierCredentials;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object [][] getCourierData() {
        return new Object[][] {
                {CourierCredentials.courierLoginWithoutLogin(courier), 400, "Недостаточно данных для входа"},
                //Без пароля тест падает, и в постмане возвращается 504, вряд ли это ожидаемо)
                {CourierCredentials.courierLoginWithoutPassword(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.courierLoginWithWrongLogin(courier), 404, "Учетная запись не найдена"},
                {CourierCredentials.courierLoginWithWrongPassword(courier), 404, "Учетная запись не найдена"},
                {CourierCredentials.courierLoginWithWrongLoginPassword(courier), 404, "Учетная запись не найдена"}
        };
    }

    @Test
    @DisplayName("Логин курьера с невалидными данными")
    public void negativeLoginCourierTest() {
        courierClient.createCourier(courier);
        ValidatableResponse deleteCourierResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = deleteCourierResponse.extract().path("id");
        ValidatableResponse response = courierClient.loginCourier(courierCredentials);
        int actualStatusCode = response.extract().statusCode();
        String actualErrorMessage = response.extract().path("message");

        assertEquals("Отличный от ожидаемого код ответа", expectedStatusCode, actualStatusCode);
        assertEquals("Неверное сообщение об ошибке", expectedErrorMessage, actualErrorMessage);
    }
}
