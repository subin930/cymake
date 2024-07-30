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
import CY.cymake.Exception.UserNotFoundException;
import CY.cymake.OpenSearch.OpenSearchService;
import CY.cymake.Repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public TotalSearchDto searchTotal(CustomUserInfoDto user, String searchBody) throws IOException {
        SearchDriveDto searchDrive = openSearchService.totalSearchFileTb(user, "tb_file", searchBody);
        SearchArchiveDto searchArchiveCar = openSearchService.totalSearchNewsTb("tb_file", "car", searchBody);
        SearchArchiveDto searchArchiveBeauty = openSearchService.totalSearchNewsTb("tb_file", "beauty", searchBody);

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
    public List<NewsResDto> changeToNewsResDto(List<NewsSearchResultDto> list) {
        List<NewsResDto> result = new ArrayList<>();
        for(NewsSearchResultDto news: list) {
            NewsResDto newsResDto = new NewsResDto(news.getTitle(), news.getUpload_date(), news.getNews_link(), news.getImg_url());
            result.add(newsResDto);
        }
        return result;
    }
}
