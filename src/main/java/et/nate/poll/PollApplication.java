package et.nate.poll;

import et.nate.poll.security.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyProperties.class)
public class PollApplication {

    public static void main(String[] args) {
        SpringApplication.run(PollApplication.class, args);
    }

}
