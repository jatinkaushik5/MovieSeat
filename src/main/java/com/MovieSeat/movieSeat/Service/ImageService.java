package com.MovieSeat.movieSeat.Service;

import com.MovieSeat.movieSeat.Configuration.CloudConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    Cloudinary cloudinary;


    public Map upload(MultipartFile file){
        String public_Id= UUID.randomUUID().toString();

        try{
            byte[] data=file.getBytes();
            Map<String,String> upload=cloudinary.uploader().upload(data,ObjectUtils.asMap(
                    "folder","MovieMax Show Booking",
                    "public_id",public_Id
            ));
            Map<String,String> result=new HashMap<>();
            result.put("publicId",public_Id);
            result.put("url",upload.get("secure_url").toString());

            return result;
        }
        catch (Exception e){
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }


    public boolean deleteImage(String publicid) throws IOException {
        cloudinary.uploader().destroy(publicid, Map.of("resource_type", "image"));
        return true;
    }
}