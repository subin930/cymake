package CY.cymake.Domain.Archive;

import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.Repository.CrwlNewsRepository;
import CY.cymake.Repository.NewsDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    private final NewsDataRepository newsDataRepository;
    private final CrwlNewsRepository crwlNewsRepository;

    public List<NewsResDto> getTotalNews(String subject) {
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
}
