package com.it.Controller;

import com.it.Payload.ApiResponse;
import com.it.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }

    // URL: http://localhost:8080/api/image/upload
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadImage(@RequestParam MultipartFile file) throws IOException {
        if (file == null || file.isEmpty() || (!"image/png".equals(file.getContentType()) && !"image/jpeg".equals(file.getContentType()))) {
            ApiResponse<Object> response = new ApiResponse<>(false, "Please insert an image in PNG or JPEG format only", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }

        String getUrl = service.uploadImage(file);
        if (getUrl != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "Image Upload Successfully", getUrl);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // URL: http://localhost:8080/api/image/create-crop-image/{id}
    @GetMapping("/create-crop-image/{id}")
    public ResponseEntity<ApiResponse<?>> getCropImageTransformationUrl(@PathVariable Long id) {
        String url = service.getCropImageTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-crop-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-rounded-corner-image/{id}
    @GetMapping("/create-rounded-corner-image/{id}")
    public ResponseEntity<ApiResponse<?>> getRoundTheCornersTransformationUrl(@PathVariable Long id) {
        String url = service.getRoundTheCornersTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-rounded-corner-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-circle-image/{id}
    @GetMapping("/create-circle-image/{id}")
    public ResponseEntity<ApiResponse<?>> getCircleTransformationUrl(@PathVariable Long id) {
        String url = service.getCircleTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-circle-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-crop-and-auto-background-color-image/{id}
    @GetMapping("/create-crop-and-auto-background-color-image/{id}")
    public ResponseEntity<ApiResponse<?>> getCropAndAutoBackgroundColorTransformationUrl(@PathVariable Long id) {
        String url = service.getCropAndAutoBackgroundColorTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-crop-and-auto-background-color-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-crop-resize/{id}
    @GetMapping("/create-crop-resize/{id}")
    public ResponseEntity<ApiResponse<?>> getResizingAndCroppingTransformationUrl(@PathVariable Long id) {
        String url = service.getResizingAndCroppingTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-crop-resize", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-gravity-auto-image/{id}
    @GetMapping("/create-gravity-auto-image/{id}")
    public ResponseEntity<ApiResponse<?>> getGravityAutoTransformationUrl(@PathVariable Long id) {
        String url = service.getGravityAutoTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-gravity-auto-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // URL: http://localhost:8080/api/image/create-Effects-and-filter-image/{id}
    @GetMapping("/create-Effects-and-filter-image/{id}")
    public ResponseEntity<ApiResponse<?>> getEffectsAndFiltersTransformationUrl(@PathVariable Long id) {
        String url = service.getEffectsAndFiltersTransformationUrl(id);
        if (url != null) {
            ApiResponse<String> response = new ApiResponse<>(true, "create-Effects-and-filter-image", url);
            return ResponseEntity.ok().body(response);
        }
        ApiResponse<String> response = new ApiResponse<>(false, "Image Not Uploaded", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
