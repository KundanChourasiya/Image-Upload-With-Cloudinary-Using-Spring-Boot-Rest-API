package com.it.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    // upload image
    public  String uploadImage(MultipartFile file) throws IOException;

    // getCropImageTransformationUrl
    public String getCropImageTransformationUrl(Long id);

    // getRoundTheCornersTransformationUrl
    public String getRoundTheCornersTransformationUrl(Long id);

    // getCircleTransformationUrl
    public String getCircleTransformationUrl(Long id);

    // getCropAndAutoBackgroundColorTransformationUrl
    public String getCropAndAutoBackgroundColorTransformationUrl(Long id);

    // getResizingAndCroppingTransformationUrl
    public String getResizingAndCroppingTransformationUrl(Long id);

    // getGravityAutoTransformationUrl
    public String getGravityAutoTransformationUrl(Long id);

    // getEffectsAndFiltersTransformationUrl
    String getEffectsAndFiltersTransformationUrl(Long id);

}
