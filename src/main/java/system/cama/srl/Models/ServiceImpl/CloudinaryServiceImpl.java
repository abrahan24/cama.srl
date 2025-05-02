package system.cama.srl.Models.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryServiceImpl {
    
    private Cloudinary cloudinary;

    public void CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "CloudStorage",
            "api_key", "647432875752392",
            "api_secret", "3FMOEFwNKAhACC24Ld02lqB9hE4",
            "secure", true
        ));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile);
        Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString(); // URL pública
    }
}
