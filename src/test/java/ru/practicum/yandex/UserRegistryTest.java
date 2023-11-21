package ru.practicum.yandex;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

public class UserRegistryTest extends BaseTest {

  @Test
  @DisplayName("Пользователь создается")
  @Description("Запрос создание пользователя вернул 200 ответ и ожидаемое тело ответа")
  public void userRegister() {
    Response response = model.sendPostRegister(user);
    model.checkStatusCode(response, 200);
    model.checkSuccess(response, true);
  }

  @Test
  @DisplayName("Нельзя создать не уникального пользователя")
  @Description("Запрос создание пользователя вернул 403 ответ и ожидаемую ошибку")
  public void nonUniqueUserRegister() {
    model.sendPostRegister(user);
    Response response = model.sendPostRegister(user);
    model.checkStatusCode(response, 403);
    model.checkExceptionMessage(response, Model.ERR_MESSAGE_NON_UNIQUE);
  }

  @Test
  @DisplayName("Нельзя создать пользователя без имени")
  @Description("Запрос создание пользователя вернул 403 ответ и ожидаемую ошибку")
  public void userRegisterWithoutName() {
    user.setName(null);
    Response response = model.sendPostRegister(user);
    model.checkStatusCode(response, 403);
    model.checkSuccess(response, false);
    model.checkExceptionMessage(response, Model.ERR_REGISTER_WITHOUT_FIELD);
  }

  @Test
  @DisplayName("Нельзя создать пользователя без пароля")
  @Description("Запрос создание пользователя вернул 403 ответ и ожидаемую ошибку")
  public void userRegisterWithoutPass() {
    user.setPassword(null);
    Response response = model.sendPostRegister(user);
    model.checkStatusCode(response, 403);
    model.checkSuccess(response, false);
    model.checkExceptionMessage(response, Model.ERR_REGISTER_WITHOUT_FIELD);
  }

  @Test
  @DisplayName("Нельзя создать пользователя без email")
  @Description("Запрос создание пользователя вернул 403 ответ и ожидаемую ошибку")
  public void userRegisterWithoutMail() {
    user.setEmail(null);
    Response response = model.sendPostRegister(user);
    model.checkStatusCode(response, 403);
    model.checkSuccess(response, false);
    model.checkExceptionMessage(response, Model.ERR_REGISTER_WITHOUT_FIELD);
  }
}
