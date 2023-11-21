package ru.practicum.yandex;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

  User user;
  Model model;

  @Before
  public void setUp() {
    RestAssured.baseURI = EnvConfig.URL_FOR_TEST_BURGER_API;
    user = new User("TestMail@yandex.ru", "testPassword1", "testUserName1");
    model = new Model();
    model.deleteUser(user);
  }

  @After
  public void deleteUser() {
    model.deleteUser(user);
  }

}
