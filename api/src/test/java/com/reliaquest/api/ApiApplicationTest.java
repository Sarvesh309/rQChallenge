package com.reliaquest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.Beans.Employee;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiApplicationTest {

    private HttpClient client;
    private String baseURL;
    private ObjectMapper mapper;
    private List<Employee> employees;

    @BeforeAll
    void init() {
        this.client = HttpClient.newHttpClient();
        this.baseURL = "http://localhost:8112/api/v1/employee";
        this.mapper = new ObjectMapper();
        this.employees = getEmployees();
    }

    @SneakyThrows
    @Test
    @Order(1)
    @DisplayName("Test 1:- Tests Get Operation ")
    void testGetAllEmployees() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode dataNode = getJsonNode(response);
        List<Employee> employees = mapper.readValue(dataNode.toString(), new TypeReference<List<Employee>>() {});

        Assertions.assertNotNull(employees);
    }

    @SneakyThrows
    @Test
    @Order(2)
    @DisplayName("Test 2:- Tests Get Operation By Id")
    void testGetEmployeeById() {
        String id = employees.get(0).getId();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode dataNode = getJsonNode(response);
        Employee employee = mapper.readValue(dataNode.toString(), new TypeReference<Employee>() {});

        Assertions.assertNotNull(employee);
    }

    @SneakyThrows
    @Test
    @Order(3)
    @DisplayName("Test 3:- Tests Create Operation ")
    void testCreateEmployee() {
        Employee employee = new Employee(UUID.randomUUID().toString(), "dummyName", 1000, 21, "dumyTitle", "dumyEmail");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employee);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = getJsonNode(response);
        Employee employee1 = mapper.readValue(jsonNode.toString(), new TypeReference<Employee>() {});
        employees.add(employee1);
        Assertions.assertEquals("dummyEmployee", employee1.getName());
    }

    @SneakyThrows
    @Test
    @Order(4)
    @DisplayName("Test 4:- Tests Delete Operation ")
    @BeforeTestMethod()
    void testDeleteEmployee(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL + "/" + "dummyEmployee"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode dataNode = getJsonNode(response);
        Boolean isDeleted = mapper.readValue(dataNode.toString(), new TypeReference<Boolean>() {});

        Assertions.assertEquals(false, isDeleted);
    }

    private JsonNode getJsonNode(HttpResponse<String> response) throws JsonProcessingException {
        String responseBody = response != null ? response.body() : null;
        return (mapper.readTree(responseBody)).get("data");
    }

    @SneakyThrows
    private List<Employee> getEmployees() {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseURL)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode dataNode = getJsonNode(response);
            return mapper.readValue(dataNode.toString(), new TypeReference<List<Employee>>() {});
    }
}
