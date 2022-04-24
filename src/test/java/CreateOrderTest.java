import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(Parameterized.class)
public class CreateOrderTest {

    private OrderClient orderClient;
    private Order order;
    private List<String> color;
    private List<String> expectedColor;


    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = Order.getRandom(color);
    }

    public CreateOrderTest(List<String> color, List<String> expectedColor) {
        this.color = color;
        this.expectedColor = expectedColor;
    }

    @Parameterized.Parameters
    public static Object[][] colorOrder() {
        return new Object[][] {
                {List.of("BLACK"), List.of("BLACK")},
                {List.of("GREY"), List.of("GREY")},
                {List.of("BLACK, GREY"), List.of("BLACK, GREY")},
                {List.of(), List.of()},
        };
    }

    @Test
    @DisplayName("Выбор цвета при создании заказа")
    @Description("Создание заказа с одним цветом, двумя, без цвета")
    public void createOrderTest() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        int trackId = response.extract().path("track");
        ValidatableResponse orderResponse = orderClient.getOrderById(trackId);
        List<Object> actualColor = orderResponse.extract().jsonPath().getList("order.color");

        assertEquals("Отличный от ожидаемого код ответа", 201, statusCode);
        assertEquals("Неверный цвет", expectedColor, actualColor);
        assertNotNull("track не возвращается", trackId);
    }

}
