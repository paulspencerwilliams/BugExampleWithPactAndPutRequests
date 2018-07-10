package wat;

import static java.util.Arrays.asList;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest
@PactTestFor(providerName = "upstream-service", port = "8080")
class FirstLevel {

  @Autowired
  AClient sut;

  @Autowired
  private ObjectMapper objectMapper;

  Long[] theData;

  @Pact(consumer = "the-consumer", provider = "upstream-service")
  public RequestResponsePact setupPact1(PactDslWithProvider builder)
      throws JsonProcessingException {

    theData = new Long[]{1L, 2L, 3L};

    return builder
        .uponReceiving("a put request with data")
        .path("/first/1/third/fourth")
        .body(objectMapper.writeValueAsString(theData))
        .method("PUT")
        .willRespondWith()
        .status(204)
        .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "setupPact1")
  public void returnsSuccessStatus1() {
    sut.putSomeData(1, asList(theData));
  }

  @Pact(consumer = "the-consumer", provider = "upstream-service")
  public RequestResponsePact setupPact2(PactDslWithProvider builder)
      throws JsonProcessingException {

    theData = new Long[]{4L, 5L, 6L};

    return builder
        .uponReceiving("another put request with data")
        .path("/first/2/third/fourth")
        .body(objectMapper.writeValueAsString(theData))
        .method("PUT")
        .willRespondWith()
        .status(204)
        .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "setupPact2")
  public void returnsSuccessStatus2() {
    sut.putSomeData(2, asList(theData));
  }
}
