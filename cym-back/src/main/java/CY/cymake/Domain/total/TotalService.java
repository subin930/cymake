package CY.cymake.Domain.total;

import CY.cymake.Domain.Archive.ArchiveService;
import CY.cymake.Domain.Archive.Dto.NewsResDto;
import CY.cymake.Domain.Auth.Dto.CustomUserInfoDto;
import CY.cymake.Domain.Drive.DriveService;
import CY.cymake.Domain.Drive.Dto.PostListResDto;
import CY.cymake.Domain.total.Dto.TotalSearchDto;
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
    public TotalSearchDto searchTotal(CustomUserInfoDto user, String searchBody) throws IOException {
        List<PostListResDto> searchDrive = driveService.searchPost(user, searchBody);
        List<NewsResDto> searchArchive1 = archiveService.searchNews("car", searchBody);
        List<NewsResDto> searchArchive2 = archiveService.searchNews("beauty", searchBody);
        // 두 리스트를 하나의 리스트로 합침
        List<NewsResDto> searchArchive = new ArrayList<>(searchArchive1);
        searchArchive.addAll(searchArchive2);
        return new TotalSearchDto(searchArchive, searchDrive);
    }
}
