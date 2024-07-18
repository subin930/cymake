package CY.cymake.Scheduler;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Scheduler {
    @Autowired
    private DataService dataService;

    //@Scheduled(fixedRate = 3600000) // 1시간마다 실행
    @Scheduled(cron = "0 30 8 * * *") //오전 8시 30분에 매일 실행
    public void scheduleDateUpdate() throws IOException {
        System.out.println("suc");
        dataService.updateNewsDB();
    }
}
