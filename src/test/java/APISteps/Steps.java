package APISteps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.И;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public  class Steps {
    public String charId, mortyRace, mortyLocation, personRace, personLocation;
    public int lastEpisode, idLastPerson;
    public String temp;

    public Steps() throws IOException {
    }

    RequestSpecification reqRickMorty = new RequestSpecBuilder()
            .setBaseUri("https://rickandmortyapi.com/api")
            .build();

    @Дано ("^Получить инфо про Морти$")
    public void getMorty(String id){
        Response gettingMorty = given()
                .spec(reqRickMorty)
                .when()
                .get("/character/" + id)
                .then()
                .extract()
                .response();
        charId = new JSONObject(gettingMorty.getBody().asString()).get("id").toString();
        mortyRace = new JSONObject(gettingMorty.getBody().asString()).get("species").toString();
        mortyLocation = new JSONObject(gettingMorty.getBody().asString()).getJSONObject("location").get("name").toString();
        System.out.println("ID Морти: " + charId);
    }
    @Затем("^Получить последний эпизод с участием Морти$")
    public void getEpisode(){
        Response gettingLastEpisode = given()
                .spec(reqRickMorty)
                .when()
                .get("/character/" + charId)
                .then()
                .extract()
                .response();
        int episode = (new JSONObject(gettingLastEpisode.getBody().asString()).getJSONArray("episode").length()-1);
        lastEpisode = Integer.parseInt(new JSONObject(gettingLastEpisode.getBody().asString())
                .getJSONArray("episode").get(episode).toString().replaceAll("[^0-9]",""));
        System.out.println("Последний эпизод где присутствовал Морти: " + lastEpisode);
    }

    @Затем("^Получить последнего персонажа в эпизоде$")
    public void getPerson() {
        Response gettingLastPerson = given()
                .spec(reqRickMorty)
                .when()
                .get("/episode/" + lastEpisode)
                .then()
                .extract()
                .response();
        int person = (new JSONObject(gettingLastPerson.getBody().asString()).getJSONArray("characters").length() - 1);
        idLastPerson = Integer.parseInt(new JSONObject(gettingLastPerson.getBody().asString())
                .getJSONArray("characters").get(person).toString().replaceAll("[^0-9]", ""));
        System.out.println("ID последнего персонажа в эпизоде: " + idLastPerson);
    }

    @Затем("^Получить информацию о последнем персонаже$")
    public void getPersonLast(){
        Response gettingParametersPerson = given()
                .spec(reqRickMorty)
                .when()
                .get("/character/" + idLastPerson)
                .then()
                .extract()
                .response();
        personRace = new JSONObject(gettingParametersPerson.getBody().asString()).get("species").toString();
        personLocation = new JSONObject(gettingParametersPerson.getBody().asString()).getJSONObject("location").get("name").toString();
        System.out.println("Данные персонажа: " + personRace + ", " + personLocation);
        System.out.println("Данные Морти: " + mortyRace +  ", " + mortyLocation);
    }

    @И("^Сравнить совпадение расы и локаций$")
    public void checkData(){
        Assert.assertEquals("Расы отличаются => ",personRace, mortyRace);
        Assert.assertEquals("Места нахождения отличаются => ",personLocation, mortyLocation);
    }


    @Затем ("^Отправить запрос, сравнив результаты$")
    public void createProject() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/1.json"))));
        body.put("name", "Tomato");
        body.put("job", "Eat market");
        Response postJson = given()
                .header("Content-type", "application/json")
                .baseUri("https://reqres.in/api")
                .body(body.toString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();
        Assertions.assertEquals((new JSONObject(postJson.getBody().asString()).get("name")), (body.get("name")), "Fail");
        Assertions.assertEquals((new JSONObject(postJson.getBody().asString()).get("job")), (body.get("job")), "Fail");
        System.out.println("ID созданного пользователя: " + (new JSONObject(postJson.getBody().asString()).get("id")));
        System.out.println("Время создания профиля: " + (new JSONObject(postJson.getBody().asString()).get("createdAt")));
    }

    @Затем ("^Авторизация на Jira$")
    public void authorizationJira() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/2.json"))));
        Response postJsonJira = given()
                .header("Content-type", "application/json")
                .baseUri("https://edujira.ifellow.ru")
                .body(body.toString())
                .when()
                .post("/rest/auth/1/session")
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println("Признаки авторизации:");
        System.out.println((new JSONObject(postJsonJira.getBody().asString())).getJSONObject("session").get("name"));
        System.out.println((new JSONObject(postJsonJira.getBody().asString())).getJSONObject("session").get("value"));
    }

}