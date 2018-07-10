package wat;

import static java.util.Arrays.asList;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FirstLevel {

  @Autowired
  AClient sut;

  @Autowired
  private ObjectMapper objectMapper;

  @Nested
  @SpringBootTest
  class SecondLevel {


    @Nested
    @SpringBootTest
    @ExtendWith(PactConsumerTestExt.class)
    @PactTestFor(providerName = "upstream-service", port = "8080")
    class FirstActualTest {

      Long[] theData;

      @Pact(consumer = "the-consumer", provider = "upstream-service")
      public RequestResponsePact setupPact(PactDslWithProvider builder)
          throws JsonProcessingException {
        Map<String, String> requestHeaders = new HashMap<>();

        Map<String, String> expectedResponseHeaders = new HashMap<>();

        theData = new Long[]{1L, 2L, 3L};

        return builder
            .uponReceiving("a put request with data")
            .path("/first/1/third/fourth")
            .body(objectMapper.writeValueAsString(theData))
            .headers(requestHeaders)
            .method("PUT")
            .willRespondWith()
            .status(204)
            .headers(expectedResponseHeaders)
            .toPact();
      }

      @Test
      @PactVerification()
      public void returnsSuccessStatus() {
        sut.putSomeData(1, asList(theData));
      }
    }


    @Nested
    @SpringBootTest
    @ExtendWith(PactConsumerTestExt.class)
    @PactTestFor(providerName = "upstream-service", port = "8080")
    class SecondActualTest {

      Long[] theData;

      @Pact(consumer = "the-consumer", provider = "upstream-service")
      public RequestResponsePact setupPact(PactDslWithProvider builder)
          throws JsonProcessingException {
        Map<String, String> requestHeaders = new HashMap<>();

        Map<String, String> expectedResponseHeaders = new HashMap<>();

        theData = new Long[]{1L, 2L, 3L};

        return builder
            .uponReceiving("another put request with data")
            .path("/first/1/third/fourth")
            .body(objectMapper.writeValueAsString(theData))
            .headers(requestHeaders)
            .method("PUT")
            .willRespondWith()
            .status(204)
            .headers(expectedResponseHeaders)
            .toPact();
      }

      @Test
      @PactVerification()
      public void returnsSuccessStatus() {
        sut.putSomeData(1, asList(theData));
      }
    }
  }
}

