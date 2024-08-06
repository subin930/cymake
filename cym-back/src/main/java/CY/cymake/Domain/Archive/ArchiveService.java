package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Archive.Dto.NewsSearchResultDto;
import CY.cymake.Domain.Archive.Dto.CrwlResDto;
import CY.cymake.Domain.Archive.Dto.CrwlTotalDto;
import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.Entity.CrwlTotalEntity;
import CY.cymake.Exception.GetNewsException;
import CY.cymake.Exception.SearchException;
import CY.cymake.OpenSearch.DataExtractor;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.CrwlNewsRepository;
import CY.cymake.Repository.CrwlTotalRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final CrwlNewsRepository crwlNewsRepository;
    private final DataExtractor dataExtractor;
    private final OpenSearchService openSearchService;
    private final CrwlTotalRepository crwlTotalRepository;

    @Transactional(readOnly = true)
    public List<NewsResDto> getTotalNews(String subject) throws IOException, Exception {
        //openSearchService.deleteNewsIndex(); //test 용. 실제 코드에서는 삭제
        List<CrwlNewsEntity> total = crwlNewsRepository.findBySubjectOrderByUploadDateDesc(subject);

        List<NewsResDto> list = new ArrayList<>();
        for(CrwlNewsEntity news: total) {
            Hibernate.initialize(news.getSummary());
            Hibernate.initialize(news.getKeywords());
            NewsResDto newsResDto = new NewsResDto(
                    news.getTitle(),
                    news.getUploadDate(),
                    news.getNewsLink(),
                    news.getImgUrl(),
                    news.getSummary(),
                    news.getKeywords()
            );
            list.add(newsResDto);
        }
        return list;
    }
    @Transactional(readOnly = true)
    public List<NewsResDto> getNews(String subject) throws Exception{
        int n = 6;
        List<CrwlNewsEntity> totalDesc = crwlNewsRepository.findBySubjectOrderByUploadDateDesc(subject);
        List<NewsResDto> list = new ArrayList<>();
        for(int i = 0; i < n; ++i) {
            CrwlNewsEntity news = totalDesc.get(i);
            Hibernate.initialize(news.getSummary());
            Hibernate.initialize(news.getKeywords());

            NewsResDto newsResDto = new NewsResDto(
                    news.getTitle(),
                    news.getUploadDate(),
                    news.getNewsLink(),
                    news.getImgUrl(),
                    news.getSummary(),
                    news.getKeywords()
            );
            list.add(newsResDto);
        }
        return list;
    }
    /*
     * 뉴스 검색
     */
    public List<NewsResDto> searchNews(String subject, String searchBody) throws Exception, IOException {

        return changeToNewsResDto(openSearchService.searchNewsTb("tb_crwl_news", subject, searchBody));
    }
    /*
     * NewsSearchResultDto -> NewsResDto
     * 검색 결과(아이디, subject, title)에서 아이디를 통해 디비에서 정보 불러오기
     */
    @Transactional(readOnly = true)
    public List<NewsResDto> changeToNewsResDto(List<NewsSearchResultDto> list) throws Exception {
        List<NewsResDto> result = new ArrayList<>();
        for(NewsSearchResultDto news: list) {
            CrwlNewsEntity newsData = crwlNewsRepository.findById(news.getNews_id()).orElseThrow(() -> new SearchException("검색에 실패하였습니다."));
            NewsResDto newsResDto = new NewsResDto(
                    newsData.getTitle(),
                    newsData.getUploadDate(),
                    newsData.getNewsLink(),
                    newsData.getImgUrl(),
                    newsData.getSummary(),
                    newsData.getKeywords()
            );
            result.add(newsResDto);
        }
        return result;
    }
    /*
     * 크롤링 총 수
     */
    public CrwlResDto getCrwlTotal() {
        List<CrwlTotalEntity> carEntity = crwlTotalRepository.findBySubjectOrderByDate("car");
        List<CrwlTotalEntity> beautyEntity = crwlTotalRepository.findBySubjectOrderByDate("beauty");
        List<CrwlTotalDto> car = convertToDtoList(carEntity);
        List<CrwlTotalDto> beauty = convertToDtoList(beautyEntity);
        return CrwlResDto.builder()
                .carCrwlData(car)
                .beautyCrwlData(beauty)
                .build();
    }

    public CrwlTotalDto convertToDto(CrwlTotalEntity entity) {
        return CrwlTotalDto.builder()
                .date(String.valueOf(entity.getDate()))
                .total(entity.getTotal())
                .build();
    }

    public List<CrwlTotalDto> convertToDtoList(List<CrwlTotalEntity> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
