package com.it.Service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.it.Entity.ImageData;
import com.it.Repository.ImageDataRepository;
import com.it.Service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {


    private Cloudinary cloudinary;

    private ImageDataRepository repository;

    public ImageServiceImpl(Cloudinary cloudinary, ImageDataRepository repository) {
        this.cloudinary = cloudinary;
        this.repository = repository;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {

        // generate random id
        String fileName = UUID.randomUUID().toString().substring(0, 5);

        // read the data
        byte[] data = new byte[file.getInputStream().available()];
        file.getInputStream().read(data);

        // upload image to Cloudinary Cloud Bucket
        String url = cloudinary.uploader()
                .upload(data, ObjectUtils.asMap("public_id", fileName))
                .get("url").toString();

        // save image data in database
        ImageData imageData = ImageData.builder()
                .imageName(fileName)
                .imageType(file.getContentType())
                .imageUrl(url)
                .build();
        repository.save(imageData);

        // return uploaded url
        return url;
    }

    @Override
    public String getCropImageTransformationUrl(Long id) {

        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            return cloudinary.url()
                    .transformation(new Transformation()
                            .width(200)
                            .height(200)
                            .crop("fill"))
                    .generate(byId.get().getImageName());
        }
        return null;
    }

    @Override
    public String getRoundTheCornersTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            String generate = cloudinary.url()
                    .transformation(new Transformation()
                            .width(200)
                            .height(200)
                            .crop("thumb")
                            .radius(20))
                    .generate(byId.get().getImageName());
            return generate;
        }
        return null;
    }

    @Override
    public String getCircleTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            String generate = cloudinary.url()
                    .transformation(new Transformation()
                            .width(200)
                            .height(200)
                            .crop("thumb").chain()
                            .radius(100).chain())
                    .generate(byId.get().getImageName());
            return generate;
        }
        return null;
    }

    @Override
    public String getCropAndAutoBackgroundColorTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            return cloudinary.url().transformation(new Transformation()
                            .crop("pad")
                            .width(300)
                            .height(400)
                            .background("auto:predominant")
                            )
                    .generate(byId.get().getImageName());
        }
        return null;
    }

    @Override
    public String getResizingAndCroppingTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            return cloudinary.url()
                    .transformation(new Transformation()
                            .width(250)
                            .height(250)
                            .gravity("faces")
                            .crop("fill"))
                    .generate(byId.get().getImageName());
        }
        return null;
    }

    @Override
    public String getGravityAutoTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            return cloudinary.url()
                    .transformation(new Transformation()
                            .width(200)
                            .height(300)
                            .gravity("auto")
                            .crop("fill"))
                    .generate(byId.get().getImageName());
        }
        return null;
    }

    @Override
    public String getEffectsAndFiltersTransformationUrl(Long id) {
        Optional<ImageData> byId = repository.findById(id);
        if (byId.isPresent()) {
            return cloudinary.url().transformation(new Transformation()
                    .effect("sepia").chain()
                    .radius("max").chain()
                    .effect("outline:100").color("lightblue").chain()
                    .background("lightblue").chain()
                    .height(300).crop("scale")).generate(byId.get().getImageName());
        }
        return null;
    }

}
