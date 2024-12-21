package com.JavaJunkie.ImageProcessor.Utils;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
public class FileUtil {
    private static final String UPLOAD_DIR="upload/";

    public static String saveFile(MultipartFile file) throws IOException {
        File directory=new File(UPLOAD_DIR);
        if(directory.exists()==false){
            directory.mkdirs();
        }
        String filePath=UPLOAD_DIR+file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return filePath;
    }

    public static boolean isValidImage(MultipartFile file){
        String contentType=file.getContentType();
        return contentType!=null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}
