package CY.cymake.Scheduler;

import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Scheduler {
    @Autowired
    private DataService dataService;

    @Autowired
    private OpenSearchService openSearchService;

    @Autowired
    private DataExtractor dataExtractor;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    //@Scheduled(cron = "0 30 8 * * *") //오전 8시 30분에 매일 실행
    public void scheduleDateUpdate() throws IOException, Exception {
        System.out.println("suc");
        dataService.updateNewsDB();
        openSearchService.bulkUploadData(dataExtractor.extractCrwlNewsData(), "tb_crwl_news", "news_id");
    }
}
