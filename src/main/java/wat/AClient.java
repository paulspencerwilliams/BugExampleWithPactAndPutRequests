package wat;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AClient {

  @Autowired
  RestTemplate restTemplate;

  public void putSomeData(int id, List<Long> listOfLongs) {
    restTemplate.put("/first/{id}/third/fourth", listOfLongs, id);
  }
}
