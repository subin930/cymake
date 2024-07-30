package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Archive.Dto.NewsSearchResultDto;
import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.CrwlNewsRepository;
import CY.cymake.Repository.NewsDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final NewsDataRepository newsDataRepository;
    private final CrwlNewsRepository crwlNewsRepository;
    private final DataExtractor dataExtractor;
    private final OpenSearchService openSearchService;

    public List<NewsResDto> getTotalNews(String subject) throws IOException, Exception {
        //openSearchService.deleteNewsIndex(); //test 용. 실제 코드에서는 삭제
        List<CrwlNewsEntity> total = crwlNewsRepository.findBySubjectOrderByUploadDateDesc(subject);
        List<NewsResDto> list = new ArrayList<>();
        for(CrwlNewsEntity news: total) {
            NewsResDto newsResDto = new NewsResDto();
            newsResDto.setTitle(news.getTitle());
            newsResDto.setUploadDate(news.getUploadDate());
            newsResDto.setNewsLink(news.getNewsLink());
            newsResDto.setImgUrl(news.getImgUrl());
            list.add(newsResDto);
        }
        return list;
    }

    public List<NewsResDto> getNews(String subject) {
        int n = 6;
        List<CrwlNewsEntity> totalDesc = crwlNewsRepository.findBySubjectOrderByUploadDateDesc(subject);
        List<NewsResDto> list = new ArrayList<>();
        for(int i = 0; i < n; ++i) {
            CrwlNewsEntity news = totalDesc.get(i);
            NewsResDto newsResDto = new NewsResDto();
            newsResDto.setTitle(news.getTitle());
            newsResDto.setUploadDate(news.getUploadDate());
            newsResDto.setNewsLink(news.getNewsLink());
            newsResDto.setImgUrl(news.getImgUrl());
            list.add(newsResDto);
        }
        return list;
    }
    /*
     * 뉴스 검색
     */
    public List<NewsResDto> searchNews(String subject, String searchBody) throws Exception, IOException {
        openSearchService.bulkUploadData(dataExtractor.extractCrwlNewsData(), "tb_crwl_news", "news_id");
        return changeToNewsResDto(openSearchService.searchNewsTb("tb_crwl_news", subject, searchBody));
    }
    /*
     * NewsSearchResultDto -> NewsResDto
     */
    public List<NewsResDto> changeToNewsResDto(List<NewsSearchResultDto> list) {
        List<NewsResDto> result = new ArrayList<>();
        for(NewsSearchResultDto news: list) {
            NewsResDto newsResDto = new NewsResDto(news.getTitle(), news.getUpload_date(), news.getNews_link(), news.getImg_url());
            result.add(newsResDto);
        }
        return result;
    }
}
