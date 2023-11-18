package ru.practicum.yandex;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderListTest {
    Order order;
    Model model;
    User user;
    String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        user = new User("TestMail@yandex.ru", "testPassword1", "testUserName1");
        order = new Order();
        model = new Model();
        model.sendPostRegister(user);
        token = model.getToken(user);
    }

    @Test
    @DisplayName("Получить список заказов авторизованного пользователя")
    @Description("Запрос получения списка заказов возвращает 200 ответ и не пустое тело ответа")
    public void getOrderListUser () {
        order.setIngredients(model.selectIngredient(0));
        model.sendPostCreateOrder(token, order);
        Response response = model.sendGetOrderList(token);
        model.checkSuccess(response, true);
        model.checkStatusCode(response, 200);
    }

    @Test
    @DisplayName("Получить список заказов не авторизованного пользователя")
    @Description("Запрос получения списка заказов возвращает 400 ответ и ожидаемый текст ошибки")
    public void getOrderListNotAuthUser () {
        order.setIngredients(model.selectIngredient(0));
        model.sendPostCreateOrder(order);
        Response response = model.sendGetOrderList();
        model.checkSuccess(response, false);
        model.checkStatusCode(response, 401);
        model.checkExceptionMessage(response, model.ERR_NOT_AUTH);
    }

    @After
    public void deleteUser() {
        model.deleteUser(user);
    }
}
