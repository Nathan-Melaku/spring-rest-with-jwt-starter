package et.nate.poll;

import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.from(PollApplication::main)
                .with(TestApplicationConfiguration.class)
                .run(args);
    }
}
