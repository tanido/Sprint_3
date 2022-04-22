import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "api/v1/orders/";
    private static final String ORDER_BY_ID_PATH = "api/v1/orders/track";


    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить список заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получить заказ по id")
    public ValidatableResponse getOrderById(int trackId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("t", trackId)
                .get(ORDER_BY_ID_PATH)
                .then();
    }
}
