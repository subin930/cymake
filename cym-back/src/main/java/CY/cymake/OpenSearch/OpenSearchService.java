package CY.cymake.OpenSearch;

import CY.cymake.Domain.Archive.Dto.NewsSearchResultDto;
import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Domain.Drive.Dto.PostSearchResultDto;
import CY.cymake.Domain.total.Dto.SearchArchiveDto;
import CY.cymake.Domain.total.Dto.SearchDriveDto;
import CY.cymake.Exception.UserNotFoundException;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.mapping.*;
import org.opensearch.client.opensearch.core.*;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.opensearch.client.opensearch.indices.IndexSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenSearchService {
    @Autowired
    private OpenSearchClient client;
    /*
     * 인덱스 생성 (tb_file 테이블)
     */
    public void createFileTb() throws IOException {
        // Create index with settings
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index("tb_file")
                .settings(new IndexSettings.Builder().numberOfShards("4").numberOfReplicas("3").build())
                .mappings(new TypeMapping.Builder()
                        .properties("file_id", new Property.Builder().long_(new LongNumberProperty.Builder().build()).build())
                        .properties("file_name", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("file_url", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("last_edit_date", new Property.Builder().date(new DateProperty.Builder().format("strict_date_optional_time||epoch_millis").build()).build())
                        .properties("post_title", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("type", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("upload_date", new Property.Builder().date(new DateProperty.Builder().format("strict_date_optional_time||epoch_millis").build()).build())
                        .properties("company_code", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("uploader", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .build()
                )
                .build();

        client.indices().create(createIndexRequest);
    }


    /*
     * 인덱스 생성 (tb_crwl_news 테이블)
     */
    public void createCrwlNewsTb() throws IOException {
        // Create index with settings
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index("tb_crwl_news")
                .settings(new IndexSettings.Builder().numberOfShards("4").numberOfReplicas("3").build())
                .mappings(new TypeMapping.Builder()
                        .properties("news_id", new Property.Builder().long_(new LongNumberProperty.Builder().build()).build())
                        .properties("img_url", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("news_link", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("subject", new Property.Builder().date(new DateProperty.Builder().build()).build())
                        .properties("title", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .properties("upload_date", new Property.Builder().date(new DateProperty.Builder().format("strict_date_optional_time||epoch_millis").build()).build())
                        .properties("url", new Property.Builder().text(new TextProperty.Builder().build()).build())
                        .build()
                )
                .build();

        client.indices().create(createIndexRequest);
    }

    /*
     * 데이터 추가(tb_file)
     */
    public void addFileData(long fileId, String fileName, String fileUrl, String lastEditDate,
                            String postTitle, String type, String uploadDate,
                            String companyCode, String uploader) throws IOException {
        IndexRequest<Map<String, Object>> request = new IndexRequest.Builder<Map<String, Object>>()
                .index("tb_file")
                .id(String.valueOf(fileId))
                .document(new HashMap<>() {{
                    put("file_id", fileId);
                    put("file_name", fileName);
                    put("file_url", fileUrl);
                    put("last_edit_date", lastEditDate);
                    put("post_title", postTitle);
                    put("type", type);
                    put("upload_date", uploadDate);
                    put("company_code", companyCode);
                    put("uploader", uploader);
                }})
                .build();

        client.index(request);
    }

    /*
     * 데이터 추가(tb_crwl_news)
     */
    public void addNewsData(long newsId, String imgUrl, String newsLink, String subject,
                            String title, String uploadDate, String url) throws IOException {
        IndexRequest<Map<String, Object>> request = new IndexRequest.Builder<Map<String, Object>>()
                .index("tb_crwl_news")
                .id(String.valueOf(newsId))
                .document(new HashMap<>() {{
                    put("news_id", newsId);
                    put("img_url", imgUrl);
                    put("news_link", newsLink);
                    put("subject", subject);
                    put("title", title);
                    put("upload_date", uploadDate);
                    put("url", url);
                }})
                .build();

        client.index(request);
    }

    /*
     * 데이터 조회
     */
    public String getFileData(long fileId) throws IOException {
        GetRequest getRequest = new GetRequest.Builder()
                .index("tb_file")
                .id(String.valueOf(fileId))
                .build();

        GetResponse<Map> response = client.get(getRequest, Map.class);
        return response.source().toString();
    }
    public String getNewsData(long newsId) throws IOException {
        GetRequest getRequest = new GetRequest.Builder()
                .index("tb_crwl_news")
                .id(String.valueOf(newsId))
                .build();

        GetResponse<Map> response = client.get(getRequest, Map.class);
        return response.source().toString();
    }

    /*
     * 데이터 삭제
     */
    public void deleteFileData(long fileId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index("tb_file")
                .id(String.valueOf(fileId))
                .build();

        client.delete(deleteRequest);
    }

    public void deleteNewsData(long newsId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest.Builder()
                .index("tb_crwl_news")
                .id(String.valueOf(newsId))
                .build();

        client.delete(deleteRequest);
    }

    /*
     * 인덱스 삭제(테이블)
     */
    public void deleteFileIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder()
                .index("tb_file")
                .build();

        client.indices().delete(deleteIndexRequest);
    }


    public void deleteNewsIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder()
                .index("tb_crwl_news")
                .build();

        client.indices().delete(deleteIndexRequest);
    }

    /*
     * 데이터 추출 및 업로드
     */
    public void bulkUploadData(List<Map<String, Object>> dataList, String tbName, String idField) throws IOException {
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

        for (Map<String, Object> data : dataList) {
            Object idValue = data.get(idField);
            String id = idValue != null ? idValue.toString() : null;

            bulkRequestBuilder.operations(op -> op.index(idx -> idx
                    .index(tbName)
                    .document(data)
                    .id(id)
            ));
        }

        BulkResponse bulkResponse = client.bulk(bulkRequestBuilder.build());
        if (bulkResponse.errors()) {
            System.out.println("Bulk upload failed with errors: " + bulkResponse);
        } else {
            System.out.println("Bulk upload succeeded.");
        }
    }
    /*
     * 데이터 검색(tb_file)
     * 1. 파일 이름
     * 2. post title
     * 조건: 회사 코드
     * 로 검색
     */
    public List<PostSearchResultDto> searchFileTb(CustomUserInfoDto user, String index, String searchBody) throws IOException {
        Set<PostSearchResultDto> resultsSet = new HashSet<>();
        String company_code = user.getCompanyCode().getCode();
        System.out.println(company_code);
        // 파일 이름으로 검색
        SearchRequest searchRequest1 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("file_name")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                        )
                )
        );

        // 검색 요청 실행
        SearchResponse<PostSearchResultDto> searchResponse1 = client.search(searchRequest1, PostSearchResultDto.class);

        // 검색 결과 리스트에 넣기
        List<Hit<PostSearchResultDto>> hits1 = searchResponse1.hits().hits();
        for (Hit<PostSearchResultDto> hit : hits1) {
            if(Objects.requireNonNull(hit.source()).getCompany_code().equals(company_code)) {
                PostSearchResultDto data = hit.source();
                resultsSet.add(data);
            }
        }

        // post title로 검색
        SearchRequest searchRequest2 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("post_title")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                        )
                )
        );

        // 검색 요청 실행
        SearchResponse<PostSearchResultDto> searchResponse2 = client.search(searchRequest2, PostSearchResultDto.class);

        // 검색 결과 리스트에 넣기
        List<Hit<PostSearchResultDto>> hits2 = searchResponse2.hits().hits();
        for (Hit<PostSearchResultDto> hit : hits2) {
            if(Objects.requireNonNull(hit.source()).getCompany_code().equals(company_code)) {
                PostSearchResultDto data = hit.source();
                resultsSet.add(data);
            }
        }
        return new ArrayList<>(resultsSet);
    }
    /*
     * 데이터 검색(tb_crwl_news)
     * 1. 뉴스 제목 title
     */

    public List<NewsSearchResultDto> searchNewsTb(String index, String subject, String searchBody) throws IOException {
        Set<NewsSearchResultDto> resultsSet = new HashSet<>();
        /*
         * 1. title로 검색
         */
        SearchRequest searchRequest1 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("title")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                                .must(m -> m
                                        .term(t -> t
                                                .field("subject")
                                                .value(FieldValue.of(subject))
                                        )
                                )
                        )
                )
        );

        //검색 요청 실행
        SearchResponse<NewsSearchResultDto> searchResponse1 = client.search(searchRequest1,NewsSearchResultDto.class);
        //검색 결과 리스트에 넣기
        List<Hit<NewsSearchResultDto>> hits1 = searchResponse1.hits().hits();
        for (Hit<NewsSearchResultDto> hit: hits1) {
            NewsSearchResultDto data = hit.source();
            resultsSet.add(data);
        }
        return new ArrayList<>(resultsSet);
    }

    /*
     * 데이터 검색(tb_file)
     * 1. 파일 이름
     * 2. post title
     * 조건: 회사 코드
     * 로 검색
     */
    public SearchDriveDto totalSearchFileTb(CustomUserInfoDto user, String index, String searchBody) throws IOException {
        SearchDriveDto searchDriveDto = new SearchDriveDto();
        Set<PostSearchResultDto> resultsSet = new HashSet<>();
        String company_code = user.getCompanyCode().getCode();
        System.out.println(company_code);
        // 파일 이름으로 검색
        SearchRequest searchRequest1 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("file_name")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                        )
                )
        );

        // 검색 요청 실행
        SearchResponse<PostSearchResultDto> searchResponse1 = client.search(searchRequest1, PostSearchResultDto.class);

        // 검색 결과 리스트에 넣기
        List<Hit<PostSearchResultDto>> hits1 = searchResponse1.hits().hits();
        for (Hit<PostSearchResultDto> hit : hits1) {
            if(Objects.requireNonNull(hit.source()).getCompany_code().equals(company_code)) {
                PostSearchResultDto data = hit.source();
                resultsSet.add(data);
            }
        }

        // post title로 검색
        SearchRequest searchRequest2 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("post_title")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                        )
                )
        );

        // 검색 요청 실행
        SearchResponse<PostSearchResultDto> searchResponse2 = client.search(searchRequest2, PostSearchResultDto.class);

        // 검색 결과 리스트에 넣기
        List<Hit<PostSearchResultDto>> hits2 = searchResponse2.hits().hits();
        for (Hit<PostSearchResultDto> hit : hits2) {
            if(Objects.requireNonNull(hit.source()).getCompany_code().equals(company_code)) {
                PostSearchResultDto data = hit.source();
                resultsSet.add(data);
            }
        }
        List<PostSearchResultDto> resultsList = new ArrayList<>(resultsSet);
        // 결과 리스트를 최신순으로 정렬하고 상위 12개만 반환
        searchDriveDto.setPostSearchResultDto(resultsList.stream()
                .sorted(Comparator.comparing(PostSearchResultDto::getUpload_date).reversed())
                .limit(10)
                .collect(Collectors.toList()));
        searchDriveDto.setNum(resultsSet.size());
        return searchDriveDto;
    }

    /*
     * 데이터 검색(tb_crwl_news)
     * 1. 뉴스 제목 title
     */
    public SearchArchiveDto totalSearchNewsTb(String index, String subject, String searchBody) throws IOException {
        SearchArchiveDto searchArchiveDto = new SearchArchiveDto();
        Set<NewsSearchResultDto> resultsSet = new HashSet<>();
        /*
         * 1. title로 검색
         */
        SearchRequest searchRequest1 = SearchRequest.of(s -> s
                .index(index)
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m
                                        .wildcard(w -> w
                                                .field("title")
                                                .value("*" + searchBody + "*")
                                        )
                                )
                                .must(m -> m
                                        .term(t -> t
                                                .field("subject")
                                                .value(FieldValue.of(subject))
                                        )
                                )
                        )
                )
        );

        //검색 요청 실행
        SearchResponse<NewsSearchResultDto> searchResponse1 = client.search(searchRequest1,NewsSearchResultDto.class);
        //검색 결과 리스트에 넣기
        List<Hit<NewsSearchResultDto>> hits1 = searchResponse1.hits().hits();
        for (Hit<NewsSearchResultDto> hit: hits1) {
            NewsSearchResultDto data = hit.source();
            resultsSet.add(data);
        }
        List<NewsSearchResultDto> resultsList = new ArrayList<>(resultsSet);
        searchArchiveDto.setNewsSearchResultDto(resultsList.stream()
                .limit(12)
                .collect(Collectors.toList()));
        searchArchiveDto.setNum(resultsSet.size());
        return searchArchiveDto;
    }
}
