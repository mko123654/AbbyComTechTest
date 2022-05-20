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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(tags = "Upload And Download APIs")
@RequestMapping("/api/file")
@Slf4j
public class UploadDownloadController {

    @ApiOperation("單一文件上傳，支援傳入參數")
    @PostMapping("/api/upload/singleAndparas")
    public String uploadFileSingle(@RequestParam("dir") String dir, @RequestParam("file") MultipartFile uploadfile) {
        return FileTransUtil.uploadFile(uploadfile, dir);
    }

    @ApiOperation("單一文件上傳，支援傳入參數、Model")
    @PostMapping("/upload/single/model")
    public String singleUploadFileModel(@ModelAttribute("model") SingleUploadModel model) {
        return FileTransUtil.uploadFile(model.getFile(), model.getDir());
    }

    @ApiOperation("多筆文件上傳，支援傳入參數")
    @PostMapping("upload/multiAndparas")
    public String uploadFileMulti(@RequestParam("dir") String dir, @RequestParam("files") MultipartFile[] uploadfiles) {
        return FileTransUtil.uploadFiles(uploadfiles, dir);
    }

    @ApiOperation("多筆文件上傳，支援傳入參數、Model")
    @PostMapping(value = "/upload/multi/model")
    public String multiUploadFileModel(@ModelAttribute(("model")) MultiUploadModel model) {
        return FileTransUtil.uploadFiles(model.getFiles(), model.getDir());
    }

    @ApiOperation("Get下載文件")
    @GetMapping(value = "/download/get")
    public ResponseEntity<InputStreamResource> downloadFileGet(@RequestParam String filePath) throws IOException {
        return FileTransUtil.downloadFile(filePath);
    }

    @ApiOperation("Post下載文件")
    @PostMapping(value = "/download/post")
    public ResponseEntity<InputStreamResource> downloadFilePost(@RequestBody DownloadQo downloadQo) throws IOException {
        return FileTransUtil.downloadFile(downloadQo.getPath());
    }
}
