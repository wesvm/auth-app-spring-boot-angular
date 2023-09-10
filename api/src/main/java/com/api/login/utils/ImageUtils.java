package com.api.login.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {
    private static final List<String> ALLOWED_IMAGE_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/jpg");
    public static boolean isImage(MultipartFile file) {
        return file != null && ALLOWED_IMAGE_TYPES.contains(file.getContentType());
    }

    public static String uploadImageToAPI(MultipartFile file) {
        String apiUrl = "https://api.bytescale.com/v2/accounts/12a1yaN/uploads/binary";
        String authToken = "Bearer public_12a1yaN6bmUdN44Avg8fSTJxC6bV";

        try{
            HttpURLConnection connection = getHttpURLConnection(file, apiUrl, authToken);
            int responseCode = connection.getResponseCode();

            if(responseCode != 200){
                return null;
            }

            InputStream inputStream = connection.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(inputStream);
            return responseJson.get("fileUrl").asText();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpURLConnection getHttpURLConnection(MultipartFile file, String apiUrl, String authToken)
            throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authToken);
        connection.setRequestProperty("Content-Type", file.getContentType());
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] fileBytes = file.getBytes();
            os.write(fileBytes);
        }
        return connection;
    }
}
