package ru.practicum.yandex;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderCreateTest {
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
    }

    @Test
    @DisplayName("Заказ с авторизацией и ингредиентами")
    @Description("Запрос создания заказа авторизованным пользователем с указанным ингредиентом возвращает 200 ответ")
    public void createOrder() {
        token = model.getToken(user);
        order.setIngredients(model.selectIngredient(0));
        Response response = model.sendPostCreateOrder(token, order);
        model.checkStatusCode(response, 200);
        model.checkSuccess(response, true);

    }

    @Test
    @DisplayName("Заказ с авторизацией и без ингредиентов")
    @Description("Запрос создания заказа авторизованным пользователем без ингредиентов возвращает 400 ошибку")
    public void createOrderWithoutIngredients() {
        token = model.getToken(user);
        Response response = model.sendPostCreateOrder(token, order);
        model.checkStatusCode(response, 400);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response,model.ERR_NOT_INGREDIENTS);
    }

    @Test
    @DisplayName("Заказ с авторизацией и некорректным ингредиентом")
    @Description("Запрос создания заказа авторизованным пользователем с некорректным ингредиентом возвращает 500 ошибку")
    public void createOrderBadHash() {
        token = model.getToken(user);
        order.setIngredients("QWERTY");
        Response response = model.sendPostCreateOrder(token, order);
        model.checkStatusCode(response, 500);
    }

    @Test
    @DisplayName("Заказ без авторизации с ингредиентами")
    @Description("Запрос создания заказа без авторизации с указанным ингредиентом возвращает 200 ответ")
    public void createOrderNoAuth() {
        order.setIngredients(model.selectIngredient(0));
        Response response = model.sendPostCreateOrder(order);
        model.checkStatusCode(response, 200);
        model.checkSuccess(response, true);
    }

    @Test
    @DisplayName("Заказ без авторизации без ингредиентов")
    @Description("Запрос создания заказа без авторизации без ингредиентов возвращает 400 ошибку")
    public void createOrderWithoutIngredientsNoAuth() {
        Response response = model.sendPostCreateOrder(order);
        model.checkStatusCode(response, 400);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response,model.ERR_NOT_INGREDIENTS);
    }

    @Test
    @DisplayName("Заказ без авторизации с некорректным ингредиентом")
    @Description("Запрос создания заказа без авторизации с некорректным ингредиентом возвращает 500 ошибку")
    public void createOrderBadHashNoAuth() {
        order.setIngredients("QWERTY");
        Response response = model.sendPostCreateOrder(order);
        model.checkStatusCode(response, 500);
    }

    @After
    public void deleteUser() {
        model.deleteUser(user);
    }


}