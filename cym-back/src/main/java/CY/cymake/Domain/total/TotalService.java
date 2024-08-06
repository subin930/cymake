package CY.cymake.Domain.total;

import CY.cymake.AWS.S3Service;
import CY.cymake.Domain.Archive.ArchiveService;
import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Archive.Dto.NewsSearchResultDto;
import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Drive.DriveService;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Domain.Drive.Dto.PostSearchResultDto;
import CY.cymake.Domain.total.Dto.SearchArchiveDto;
import CY.cymake.Domain.total.Dto.SearchDriveDto;
import CY.cymake.Domain.total.Dto.TotalSearchDto;
import CY.cymake.Entity.CrwlNewsEntity;
import CY.cymake.Exception.SearchException;
import CY.cymake.Exception.UserNotFoundException;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.CrwlNewsRepository;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TotalService {
    private final DriveService driveService;
    private final ArchiveService archiveService;
    private final UsersRepository usersRepository;
    private final OpenSearchService openSearchService;
    private final S3Service s3Service;
    private final CrwlNewsRepository crwlNewsRepository;

    public TotalSearchDto searchTotal(CustomUserInfoDto user, String searchBody) throws IOException, SearchException {
        SearchDriveDto searchDrive = openSearchService.totalSearchFileTb(user, "tb_file", searchBody);
        SearchArchiveDto searchArchiveCar = openSearchService.totalSearchNewsTb("tb_crwl_news", "car", searchBody);
        SearchArchiveDto searchArchiveBeauty = openSearchService.totalSearchNewsTb("tb_crwl_news", "beauty", searchBody);

        return new TotalSearchDto(searchArchiveCar.getNum(), changeToNewsResDto(searchArchiveCar.getNewsSearchResultDto()),
                searchArchiveBeauty.getNum(), changeToNewsResDto(searchArchiveBeauty.getNewsSearchResultDto()),
                searchDrive.getNum(), changeToPostList(user, searchDrive.getPostSearchResultDto()));
    }
    /*
     * PostListDto -> PostSearchResultDto
     */
    public List<PostListResDto> changeToPostList(CustomUserInfoDto user, List<PostSearchResultDto> list) throws IOException {
        String directory = "files/" + user.getCompanyCode().getCode() + "/";
        List<PostListResDto> result = new ArrayList<>();
        for(PostSearchResultDto post: list) {
            String username = usersRepository.findById(post.getUploader()).orElseThrow(() -> new UserNotFoundException("파일 업로더가 존재하지 않습니다.")).getUsername();
            PostListResDto postListResDto = new PostListResDto(post.getFile_name(), post.getPost_title(), post.getFile_url(), post.getUploader(), username, post.getUpload_date(), s3Service.getFileSize(directory,post.getFile_name()));
            result.add(postListResDto);
        }
        return result;
    }
    /*
     * NewsSearchResultDto -> NewsResDto
     */
    @Transactional(readOnly = true)
    public List<NewsResDto> changeToNewsResDto(List<NewsSearchResultDto> list) throws SearchException {
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
}
