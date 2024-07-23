package CY.cymake.Domain.Drive;

import CY.cymake.Domain.Drive.Dto.Item;
import CY.cymake.Entity.UsersEntity;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchDriveService {
    private final ElasticsearchClient esClient;
    private final JdbcTemplate jdbcTemplate;

    //데이터 인덱싱 -> Items에
    public void indexData() throws IOException {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM tb_file");

        for(Map<String, Object> row: rows) {
            String fileName = row.get("file_name").toString();
            String postTitle = row.get("post_title").toString();
            String uploader = row.get("uploader").toString();

            Item item = Item.builder()
                    .fileName(fileName)
                    .postTitle(postTitle)
                    .uploader(uploader)
                    .build();
            IndexRequest<Item> indexRequest = IndexRequest.of(i -> i
                    .index("Items")
                    .document(item));
            esClient.index(indexRequest);
        }
    }

    public List<Item> searchDrive(String searchBody) throws IOException {
        final String INDEX = "items";
        SearchResponse<Item> search = esClient.search(s -> s
                .index("Items")
                .query(q -> q
                        .term(t -> t
                                .field("file_name")
                                .value(v -> v.stringValue(searchBody))
                        )),
                Item.class);
        List<Item> items = new ArrayList<>();
        //검색 결과 처리
        for(Hit<Item> hit: search.hits().hits()) {
            Item item = hit.source();
            items.add(item);
        }
        return items;
    }
}
