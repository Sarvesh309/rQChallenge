package com.reliaquest.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.Beans.Employee;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String baseURL = "http://localhost:8112/api/v1/employee";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static List<Employee> employeeList;
    private static final String data = "data";

    /**
     *
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @SneakyThrows
    public static List<Employee> getAllEmployee() {
        String responseBody = fetchJsonData();
        JsonNode dataNode = (mapper.readTree(responseBody)).get(data);
        if (dataNode != null)
            employeeList = mapper.readValue(dataNode.toString(), new TypeReference<List<Employee>>() {});

        return employeeList;
    }

    /**
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @SneakyThrows
    private static String fetchJsonData(){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            log.info("Successfully fetched Employees data, Status Code: {}", 200);
            return response.body();
        }
        else {
            log.error("Failed to fetch employee data, HTTP Status Code: {}", response.statusCode());
            return null;
        }
    }

    /**
     *
     * @param searchString
     * @return
     */
    public static List<Employee> getEmployeesByNameSearch(String searchString) {
        return employeeList.stream().filter(Objects::nonNull).
                filter(employee -> employee.getName().equalsIgnoreCase(searchString))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     *
     * @param id
     * @return
     */
    public static @Nullable Employee getEmployeeById(@NonNull String id) {
        return employeeList.stream().filter(Objects::nonNull).
                filter(employee -> String.valueOf(employee.getId()).equals(id)).
                findFirst().
                orElse(null);
    }

    /**
     *
     * @return
     */
    public static Integer getHighestSalaryOfEmployees() {
        return employeeList.stream().map(Employee::getSalary).
                max(Integer::compare)
                .orElseThrow();
    }

    /**
     *
     * @return
     */
    public static List<String> getTopTenHighestEarningEmployeeNames() {
        return employeeList.stream().
                sorted(Comparator.comparingInt(Employee::getSalary).reversed()).
                limit(10).
                map(Employee::getName).
                collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *
     * @param employeeInput
     * @return
     * @param <Input>
     * @throws IOException
     * @throws InterruptedException
     */
    @SneakyThrows
    public static <Input> Employee createEmployee(@RequestBody Input employeeInput)  {

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeInput);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            log.info("Successfully created Employee data ");
            JsonNode dataNode = getJsonNode(response);
            return mapper.readValue(dataNode.toString(), new TypeReference<Employee>() {});
        }
        else {
            log.error("Failed to fetch employee data, HTTP Status Code: {}: ", response.statusCode());
            return null;
        }
    }

    private static JsonNode getJsonNode(HttpResponse<String> response) throws JsonProcessingException {
        String responseBody = response != null ? response.body() : null;
        return (mapper.readTree(responseBody)).get(data);
    }

    /**
     *
     * @param id
     * @return
     */
    @SneakyThrows
    public static String deleteEmployeeById(@NonNull String id)  {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseURL + "/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            log.info("Successfully deleted Employee data, Status Code: {}: ", 200);
            return response.body();
        }
        else {
            log.error("Failed to Delete employee data, HTTP Status Code: {}", response.statusCode());
            return null;
        }
    }
}