package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class OrderListTest extends BaseTest {

  private Order order;
  private String token;

  @Before
  public void setUpForOrderList() {
    order = new Order();
    model.sendPostRegister(user);
    token = model.getToken(user);
  }

  @Test
  @DisplayName("Получить список заказов авторизованного пользователя")
  @Description("Запрос получения списка заказов возвращает 200 ответ и не пустое тело ответа")
  public void getOrderListUser() {
    order.setIngredients(model.selectIngredient(0));
    model.sendPostCreateOrder(token, order);
    Response response = model.sendGetOrderList(token);
    model.checkSuccess(response, true);
    model.checkStatusCode(response, 200);
  }

  @Test
  @DisplayName("Получить список заказов не авторизованного пользователя")
  @Description("Запрос получения списка заказов возвращает 400 ответ и ожидаемый текст ошибки")
  public void getOrderListNotAuthUser() {
    order.setIngredients(model.selectIngredient(0));
    model.sendPostCreateOrder(order);
    Response response = model.sendGetOrderList();
    model.checkSuccess(response, false);
    model.checkStatusCode(response, 401);
    model.checkExceptionMessage(response, Model.ERR_NOT_AUTH);
  }
}
