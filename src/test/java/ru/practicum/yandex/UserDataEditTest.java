package ru.practicum.yandex;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataEditTest {
    private User user;
    private User user2;
    private Model model;
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api/";
        user = new User("TestMail@yandex.ru", "testPassword1", "testName1");
        user2 = new User();
        model = new Model();
        model.deleteUser(user);
        model.sendPostRegister(user);
    }

    @Test
    @DisplayName("Можно поменять имя пользователя")
    @Description("Запрос изменения данных вернул новое имя пользователя")
    public void dataUserName() {
        String newName = "newTestName1";
        token = model.getToken(user);
        user2.setName(newName);
        Response response = model.sendEditDataUser(token, user2);
        model.checkStatusCode(response,200);
        model.checkSuccess(response, true);
        response.then().assertThat().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Можно поменять email пользователя")
    @Description("Запрос изменения данных вернул новой email пользователя")
    public void dataUserMail() {
        String newMail = "newTestMail@yandex.ru";
        token = model.getToken(user);
        user2.setEmail(newMail);
        Response response = model.sendEditDataUser(token, user2);
        model.checkStatusCode(response,200);
        model.checkSuccess(response, true);
        response.then().assertThat().body("user.email", equalTo(newMail.toLowerCase()));
        user.setEmail(newMail);
    }

    @Test
    @DisplayName("Нельзя изменить имя пользователя без авторизации")
    @Description("Запрос изменения данных вернул 401 ответ и ожидаемую ошибку")
    public void dataUserNameNoAuth() {
        String newName = "newTestName1";
        user2.setName(newName);
        Response response = model.sendEditDataUser(user2);
        model.checkStatusCode(response,401);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response, model.ERR_NOT_AUTH);
    }

    @Test
    @DisplayName("Нельзя изменить email пользователя без авторизации")
    @Description("Запрос изменения данных вернул 401 ответ и ожидаемую ошибку")
    public void dataUserMailNoAuth() {
        String newMail = "newTestMail@yandex.ru";
        user2.setEmail(newMail);
        Response response = model.sendEditDataUser(user2);
        model.checkStatusCode(response,401);
        model.checkSuccess(response, false);
        model.checkExceptionMessage(response, model.ERR_NOT_AUTH);
    }

    @After
    public void deleteUser() {
        model.deleteUser(user);
    }
}
