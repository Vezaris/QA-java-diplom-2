package ru.practicum.yandex;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class UserAuthTest {
    private User user;
    private Model model;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        user = new User("TestMail@yandex.ru", "testPassword1", "testUserName1");
        model = new Model();
        model.deleteUser(user);
        model.sendPostRegister(user);
    }

    @Test
    @DisplayName("Пользователь авторизуется")
    @Description("Запрос авторизации пользователя вернул 200 ответ и ожидаемое тело ответа")
    public void authUser() {
        Response response = model.sendPostAuth(user);
        model.checkStatusCode(response, 200);
        model.checkSuccess(response, true);
    }

    @Test
    @DisplayName("Нельзя авторизоваться без email")
    @Description("Запрос авторизации пользователя вернул 401 ответ и ожидаемую ошибку")
    public void authUserWithoutMail() {
        user.setEmail(null);
        Response response = model.sendPostAuth(user);
        model.checkStatusCode(response, 401);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response, model.ERR_AUTH_WITHOUT_FIELD);
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    @Description("Запрос авторизации пользователя вернул 401 ответ и ожидаемую ошибку")
    public void authUserWithoutPass() {
        user.setPassword(null);
        Response response = model.sendPostAuth(user);
        model.checkStatusCode(response, 401);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response, model.ERR_AUTH_WITHOUT_FIELD);
    }

    @After
    public void deleteUser() {
        model.deleteUser(user);
    }
}