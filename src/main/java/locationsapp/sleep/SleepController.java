package locationsapp.sleep;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@Slf4j
public class SleepController {

    private Random randomGenerator = new Random();

    @GetMapping("/api/sleep")
    @SneakyThrows
    public SleepResponse sleep(@RequestParam(defaultValue = "0") int time, @RequestParam(defaultValue = "false") boolean random) {
        var calculatedTime = time;
        if (random) {
            calculatedTime = randomGenerator.nextInt(time);
        }

        var uuid = UUID.randomUUID().toString();
        log.info("Sleeping {} for: {} sec", uuid, calculatedTime);
        Thread.sleep(calculatedTime);
        log.info("Return after sleep: {}", uuid);
        return new SleepResponse(calculatedTime);
    }


}
