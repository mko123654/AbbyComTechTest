/***********************************************************
 * @Description : 文件上傳、下載RESTful API
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-20 19:11
 * @email       : mko123654@gmail.com
 ***********************************************************/

package abby.exam.controller;

import abby.exam.qo.DownloadQo;
import abby.exam.qo.MultiUploadModel;
import abby.exam.qo.SingleUploadModel;
import abby.exam.utils.FileTransUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Tag(name = "Upload And Download APIs")
@RequestMapping("/api/file")
@Slf4j
public class UploadDownloadController {

    @PostMapping("/api/upload/singleAndparas")
    @Operation(summary = "", description = "單一文件上傳，支援傳入參數")
    public String uploadFileSingle(@RequestParam("dir") String dir, @RequestParam("file") MultipartFile uploadfile) {
        return FileTransUtil.uploadFile(uploadfile, dir);
    }

    @PostMapping("/upload/single/model")
    @Operation(summary = "", description = "單一文件上傳，支援傳入參數、Model")
    public String singleUploadFileModel(@ModelAttribute("model") SingleUploadModel model) {
        return FileTransUtil.uploadFile(model.getFile(), model.getDir());
    }

    @PostMapping("upload/multiAndparas")
    @Operation(summary = "", description = "多筆文件上傳，支援傳入參數")
    public String uploadFileMulti(@RequestParam("dir") String dir, @RequestParam("files") MultipartFile[] uploadfiles) {
        return FileTransUtil.uploadFiles(uploadfiles, dir);
    }

    @PostMapping(value = "/upload/multi/model")
    @Operation(summary = "", description = "多筆文件上傳，支援傳入參數、Model")
    public String multiUploadFileModel(@ModelAttribute(("model")) MultiUploadModel model) {
        return FileTransUtil.uploadFiles(model.getFiles(), model.getDir());
    }

    @GetMapping(value = "/download/get")
    @Operation(summary = "", description = "Get下載文件")
    public ResponseEntity<InputStreamResource> downloadFileGet(@RequestParam String filePath) throws IOException {
        return FileTransUtil.downloadFile(filePath);
    }

    @PostMapping(value = "/download/post")
    @Operation(summary = "", description = "Post下載文件")
    public ResponseEntity<InputStreamResource> downloadFilePost(@RequestBody DownloadQo downloadQo) throws IOException {
        return FileTransUtil.downloadFile(downloadQo.getPath());
    }
}
