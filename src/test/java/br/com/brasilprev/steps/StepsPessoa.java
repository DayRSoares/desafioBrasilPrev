package br.com.brasilprev.steps;

import br.com.brasilprev.utils.DataPool;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.FileNotFoundException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StepsPessoa {

    private Response response;
    private JsonPath dataPool;

    public StepsPessoa() throws FileNotFoundException {
        dataPool = DataPool.dataPool("massaDados.json");
    }

    @Given("^serviço esteja no ar$")
    public void servicoEstejaDisponivel() {
        RestAssured.baseURI = "http://localhost:8080";

    }

    String pessoaT = " {\n" +
            "      \"codigo\": 0,\n" +
            "      \"nome\": \"Jeremias Santos da Silva\",\n" +
            "      \"cpf\": \"32541221542\",\n" +
            "      \"enderecos\": [\n" +
            "        {\n" +
            "          \"logradouro\": \"Quadra 4 Conjunto 24\",\n" +
            "          \"numero\": 196,\n" +
            "          \"complemento\": \"Bairro\",\n" +
            "          \"bairro\": \"Bairro Sul  (Bairro Sul - Riacho)\",\n" +
            "          \"cidade\": \"Distrito Federal\",\n" +
            "          \"estado\": \"DF\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"telefones\": [\n" +
            "        {\n" +
            "          \"ddd\": \"61\",\n" +
            "          \"numero\": \"981007641\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }";

    @When("^incluir pessoa$")
    public void devoIncluirUmaPessoaViaPOST() {

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(pessoaT)
                .when()
                .post("/pessoas")
                .then()
                .extract().response();
    }

    @Then("^verificar se uma pessoa foi incluída \"([^\"]*)\"$")
    public void VerificarSeUmaPessoaFoiIncluida(String pessoa) {

        assertEquals(pessoa, response.jsonPath().getString("nome"));
        assertEquals(201, response.statusCode());
    }

    @When("consultar uma pessoa \"([^\"]*)\" , \"([^\"]*)\"$")
    public void ConsultarUmaPessoa(String ddd, String telefone) {

        response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pessoas/" + ddd + "/" + telefone + "")
                .then()
                .extract().response();

    }

    @Then("^validar campo no body da consulta \"([^\"]*)\"$")
    public void validarCampoNoBodyDaConsulta(String field) {

        assertEquals(200, response.statusCode());
        assertEquals(field, response.jsonPath().getString("nome"));
    }


    @Then("^verificar status code e mensagens de resposta (\\d+), \"([^\"]*)\"$")
    public void verificarStatusCodeEMensagensDeResposta(int statusCode, String mensagem) {
        validadorDeBody(statusCode, mensagem);
    }

    String pessoa2 = "  {\n" +
            "      \"codigo\": 0,\n" +
            "      \"nome\": \"Jeremias Santos da Silva\",\n" +
            "      \"cpf\": \"%s\",\n" +
            "      \"enderecos\": [\n" +
            "        {\n" +
            "          \"logradouro\": \"Quadra 4 Conjunto 24\",\n" +
            "          \"numero\": 196,\n" +
            "          \"complemento\": \"Bairro\",\n" +
            "          \"bairro\": \"Bairro Sul  (Bairro Sul - Riacho)\",\n" +
            "          \"cidade\": \"Distrito Federal\",\n" +
            "          \"estado\": \"DF\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"telefones\": [\n" +
            "        {\n" +
            "          \"ddd\": \"%s\",\n" +
            "          \"numero\": \"%s\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }";


    @When("^incluir pessoa com CPF cadastrado \"([^\"]*)\"$")
    public void IncluirPessoaComCPFCadastrado(String cpf) {

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(String.format(pessoa2, cpf, "62", "781007641"))
                .when()
                .post("/pessoas")
                .then()
                .extract().response();

    }


    @Then("^validar se a pessoa não foi incluida (\\d+) \"([^\"]*)\"$")
    public void validarSeAPessoaNaoFoiIncluida(int statusCode, String mensagem) {
        validadorDeBody(statusCode, mensagem);
    }

    public void validadorDeBody(int statusCode, String mensagem) {
        assertEquals(statusCode, response.statusCode());
        assertThat(mensagem, containsString(mensagem));
    }

    @When("^incluir uma pessoa com ddd e telefone cadastrados \"([^\"]*)\", \"([^\"]*)\"$")
    public void IncluirUmaPessoaComDddETelefoneCadastrados(String ddd, String telefone) {
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(String.format(dataPool.getString("PESSOAS[1]"), "32541221542", ddd, telefone))
                .when()
                .post("/pessoas")
                .then()
                .extract().response();
    }
}
