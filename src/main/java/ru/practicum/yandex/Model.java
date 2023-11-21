package ru.practicum.yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Model {

            /*** Эндпоинты ***/
  private static final String REGISTER_USER = "auth/register";
  private static final String AUTH_USER = "auth/login";
  private static final String EDIT_USER = "auth/user";
  private static final String ORDERS = "orders";
  private static final String INGREDIENTS = "ingredients";

           /*** Тексты ошибок ***/
  public static final String ERR_MESSAGE_NON_UNIQUE = "User already exists";
  public static final String ERR_REGISTER_WITHOUT_FIELD = "Email, password and name are required fields";
  public static final String ERR_AUTH_WITHOUT_FIELD = "email or password are incorrect";
  public static final String ERR_NOT_AUTH = "You should be authorised";
  public static final String ERR_NOT_INGREDIENTS = "Ingredient ids must be provided";

            /*** Шаги ***/
  @Step("Отправить запрос на регистрацию пользователя")
  public Response sendPostRegister(User user) {
    return given()
        .header("Content-type", "application/json")
        .body(user)
        .when()
        .post(REGISTER_USER);
  }

  @Step("Отправить запрос на авторизацию пользователя")
  public Response sendPostAuth(User user) {
    return given()
        .header("Content-type", "application/json")
        .body(user)
        .when()
        .post(AUTH_USER);
  }

  @Step("Отправить запрос на удаление пользователя")
  public void sendDeleteUser(String token) {
    given()
        .header("Content-type", "application/json")
        .auth().oauth2(token)
        .delete(EDIT_USER);
  }

  @Step("Отправить запрос на изменение информации о пользователе")
  public Response sendEditDataUser(String token, User user) {
    return given()
        .header("Content-type", "application/json")
        .auth().oauth2(token)
        .body(user)
        .patch(EDIT_USER);
  }

  @Step("Отправить запрос на изменение информации о пользователе")
  public Response sendEditDataUser(User user) {
    return given()
        .header("Content-type", "application/json")
        .body(user)
        .patch(EDIT_USER);
  }

  @Step("Сравнить текст в теле ошибки")
  public void checkExceptionMessage(Response response, String errMessage) {
    response.then().assertThat().body("message", equalTo(errMessage));
  }

  @Step("Сравнить код ответа")
  public void checkStatusCode(Response response, int code) {
    response.then().statusCode(code);
  }

  @Step("Проверить успех по полю Success")
  public void checkSuccess(Response response, boolean value) {
    response.then().body("success", equalTo(value));
  }

  @Step("Получить токен авторизации пользователя")
  public String getToken(User user) {
    return sendPostAuth(user).then().extract().path("accessToken").toString().substring(7);
  }

  @Step("Удалить пользователя")
  public void deleteUser(User user) {
    try {
      String token = getToken(user);
      sendDeleteUser(token);
    } catch (NullPointerException exception) {
      System.out.println("TIPS: Удалять не нужно");
    }
  }

  @Step("Отправить запрос на создание заказа")
  public Response sendPostCreateOrder(Order order) {
    return given()
        .header("Content-type", "application/json")
        .body(order)
        .when()
        .post(ORDERS);
  }

  @Step("Отправить запрос на создание заказа")
  public Response sendPostCreateOrder(String token, Order order) {
    return given()
        .header("Content-type", "application/json")
        .auth().oauth2(token)
        .body(order)
        .when()
        .post(ORDERS);
  }

  @Step("Получить список ингредиентов")
  public Response sendGetIngredients() {
    return given()
        .header("Content-type", "application/json")
        .get(INGREDIENTS);
  }

  @Step("Выбрать ингредиент из списка")
  public String selectIngredient(int index) {
    List<String> ingredients =
        sendGetIngredients().then().extract().path("data._id");
    return ingredients.get(index);
  }

  @Step("Отправить запрос на получение списка заказов пользователя")
  public Response sendGetOrderList(String token) {
    return given()
        .header("Content-type", "application/json")
        .auth().oauth2(token)
        .get(ORDERS);
  }

  @Step("Отправить запрос на получение списка заказов пользователя")
  public Response sendGetOrderList() {
    return given()
        .header("Content-type", "application/json")
        .get(ORDERS);
  }
}
