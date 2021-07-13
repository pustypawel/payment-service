package dev.pusty.paymentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pusty.paymentservice.api.PaymentResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServiceIT {
    private final Path createPaymentFileJson = getResource("create-payment.json");
    private final Path updatePaymentFileJson = getResource("update-payment.json");

    private static Path getResource(String fileName) {
        try {
            return Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private URI serviceURI() {
        return URI.create("http://localhost:" + port + "/api/payments/");
    }

    private URI resourceURI(final String paymentId) {
        return serviceURI().resolve(paymentId);
    }

    @Test
    public void should_do_all_crud_operations() throws IOException, InterruptedException {
        //create
        final HttpResponse<String> createResponse = sendCreateRequestFromFile();
        assertTrue(isSuccess(createResponse.statusCode()));
        final String paymentId = objectMapper.readValue(createResponse.body(), PaymentResource.class).getId();

        //update
        final HttpResponse<String> updateResponse = sendUpdateRequestFromFile(paymentId);
        assertTrue(isSuccess(updateResponse.statusCode()));

        //read all
        final HttpResponse<String> getAllResponse = sendGetAllRequest();
        assertTrue(isSuccess(getAllResponse.statusCode()));

        //read
        final HttpResponse<String> getResponse = sendGetOneRequest(paymentId);
        assertTrue(isSuccess(getResponse.statusCode()));

        //delete
        final HttpResponse<String> deleteResponse = sendDeleteRequest(paymentId);
        assertTrue(isSuccess(deleteResponse.statusCode()));
    }

    @Test
    public void should_not_found() throws IOException, InterruptedException {
        final String notExistingPaymentId = UUID.randomUUID().toString();
        //update
        HttpResponse<String> updateResponse = sendUpdateRequestFromFile(notExistingPaymentId);
        assertTrue(isNotFound(updateResponse.statusCode()));

        //get one
        HttpResponse<String> getOneResponse = sendGetOneRequest(notExistingPaymentId);
        assertTrue(isNotFound(updateResponse.statusCode()));

        //delete
        HttpResponse<String> deleteResponse = sendDeleteRequest(notExistingPaymentId);
        assertTrue(isNotFound(updateResponse.statusCode()));
    }

    @Test
    public void should_bad_request() throws IOException, InterruptedException {
        //create
        HttpResponse<String> createResponse = sendCreateRequest(HttpRequest.BodyPublishers.noBody());
        assertTrue(isBadRequest(createResponse.statusCode()));

        //update
        HttpResponse<String> updateResponse = sendUpdateRequest("not_important", HttpRequest.BodyPublishers.noBody());
        assertTrue(isBadRequest(updateResponse.statusCode()));
    }

    private HttpResponse<String> sendCreateRequestFromFile() throws IOException, InterruptedException {
        return sendCreateRequest(HttpRequest.BodyPublishers.ofFile(createPaymentFileJson));
    }

    private HttpResponse<String> sendCreateRequest(final HttpRequest.BodyPublisher bodyPublisher) throws IOException, InterruptedException {
        final HttpRequest createRequest = HttpRequest.newBuilder(serviceURI())
                .POST(bodyPublisher)
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendUpdateRequestFromFile(final String paymentId) throws IOException, InterruptedException {
        return sendUpdateRequest(paymentId, HttpRequest.BodyPublishers.ofFile(updatePaymentFileJson));
    }

    private HttpResponse<String> sendUpdateRequest(final String paymentId,
                                                   final HttpRequest.BodyPublisher bodyPublisher) throws IOException, InterruptedException {
        final HttpRequest updateRequest = HttpRequest.newBuilder(resourceURI(paymentId))
                .PUT(bodyPublisher)
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(updateRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendGetOneRequest(final String paymentId) throws IOException, InterruptedException {
        return sendGetRequest(resourceURI(paymentId));
    }

    private HttpResponse<String> sendGetAllRequest() throws IOException, InterruptedException {
        return sendGetRequest(serviceURI());
    }

    private HttpResponse<String> sendGetRequest(final URI uri) throws IOException, InterruptedException {
        final HttpRequest getAllRequest = HttpRequest.newBuilder(uri)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(getAllRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDeleteRequest(final String paymentId) throws IOException, InterruptedException {
        final HttpRequest deleteRequest = HttpRequest.newBuilder(resourceURI(paymentId))
                .DELETE()
                .header("Content-Type", "application/json")
                .build();
        return httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
    }

    private boolean isSuccess(final int statusCode) {
        return statusCode >= 200
                && statusCode < 300;
    }

    private boolean isNotFound(final int statusCode) {
        return statusCode == 404;
    }

    private boolean isBadRequest(final int statusCode) {
        return statusCode == 400;
    }
}
