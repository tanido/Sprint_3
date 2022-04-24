import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetListOfOrdersTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrdersTest() {
        ValidatableResponse response = orderClient.getOrderList();
        int statusCode = response.extract().statusCode();
        List<Map<String, Object>> orderList = response.extract().jsonPath().getList("orders");

        assertEquals("Отличный от ожидаемого код ответа", 200, statusCode);
        assertNotNull("Список заказов пуст", orderList);
    }
}
