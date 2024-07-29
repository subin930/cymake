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
        List<PostListResDto> searchDrive = driveService.totalSearchPost(user, searchBody);
        List<NewsResDto> searchArchiveCar = archiveService.totalSearchNews("car", searchBody);
        List<NewsResDto> searchArchiveBeauty = archiveService.totalSearchNews("beauty", searchBody);

        return new TotalSearchDto(searchArchiveCar, searchArchiveBeauty, searchDrive);
    }
}
