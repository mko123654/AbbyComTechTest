/***********************************************************
 * @Description : 文件傳輸的物件(單一文件)
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-12 23:56
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleUploadModel {

    private MultipartFile file;
    private String dir;
}
