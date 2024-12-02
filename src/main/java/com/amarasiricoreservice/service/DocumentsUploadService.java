package com.amarasiricoreservice.service;

import com.amarasiricoreservice.util.MediaFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;



@Service
@Transactional
public class DocumentsUploadService {

//    public String uploadLeaseDocuments(MultipartFile image, String uploadFolder) throws IOException {
//        if (image.isEmpty()) {
//            throw new IOException("File is empty and cannot be uploaded.");
//        }
//        System.out.println("upload start");
//
//        long localTime = new java.util.Date().getTime();
//        String fileName = localTime + "_" + image.getOriginalFilename();
//        String filePath = uploadFolder + File.separator + fileName;
//
//        // Use try-with-resources to ensure proper closure of streams
//        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//            fileOut.write(image.getBytes());
//        } catch (IOException e) {
//            // Consider logging the exception and possibly rethrowing it
//            throw new IOException("Error saving file: " + fileName, e);
//        }
//
//        // Return the URL based on how you serve static files
//        return "/images/" + fileName;
//    }


    @Autowired
    private ServletContext servletContext;
    public String uploadLeaseDocuments(MultipartFile image, String uploadFolder) throws IOException {
        if (image.isEmpty()) {
            throw new IOException("File is empty and cannot be uploaded.");
        }

        long localTime = new java.util.Date().getTime();
        String fileName = localTime + "_" + image.getOriginalFilename();

        // Ensure the upload folder exists
        File uploadDir = new File(uploadFolder);
        if (!uploadDir.exists()) {
            boolean isCreated = uploadDir.mkdirs();
            if (!isCreated) {
                throw new IOException("Failed to create directory: " + uploadFolder);
            }
        }

        // Construct the file path
        String filePath = Paths.get(uploadFolder, fileName).toString();

        // Use try-with-resources to write the file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            fileOut.write(image.getBytes());
        } catch (IOException e) {
            throw new IOException("Error saving file: " + fileName, e);
        }

        // Return the URL for accessing the image
        return "/uploads/" + fileName;
    }

//public String uploadLeaseDocuments(MultipartFile image, String uploadFolder) throws IOException {
//    if (image.isEmpty()) {
//        throw new IOException("File is empty and cannot be uploaded.");
//    }
//
//    System.out.println("Upload started");
//
//    long localTime = new java.util.Date().getTime();
//    String fileName = localTime + "_" + image.getOriginalFilename();
//
//    // Ensure the upload folder exists
//    File uploadDir = new File(uploadFolder);
//    if (!uploadDir.exists()) {
//        boolean isCreated = uploadDir.mkdirs(); // Capture the result of mkdirs()
//        if (!isCreated) {
//            throw new IOException("Failed to create directory: " + uploadFolder);
//        }
//    }
//
//    // Construct the file path
//    String filePath = Paths.get(uploadFolder, fileName).toString();
//
//    // Use try-with-resources to ensure proper closure of streams
//    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//        fileOut.write(image.getBytes());
//    } catch (IOException e) {
////        logger.error("Error saving file: " + fileName, e);
//        throw new IOException("Error saving file: " + fileName, e);
//    }
//
//    // Return the URL to access the image (assuming you're serving static files via /images/**)
//    return "/uploads/" + fileName;
//}


}
