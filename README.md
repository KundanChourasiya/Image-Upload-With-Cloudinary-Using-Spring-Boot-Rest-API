# Image-Upload-With-Cloudinary-Using-Spring-Boot-Rest-API

> [!TIP]
> If you're ready to get coding and documentions, jump to: https://cloudinary.com/documentation/java_integration

> [!WARNING]
> ### Cloudinary API Prerequisite
> * Create Cloudinary Account.
> * We get Cloud access key, secret access key  and Cloud name to access the Cloudinary from application development environment.


> [!NOTE]
> ### In this Api we upload and Transformation the image using Cloudinary API integration.
> 1. Create Cloudinary account
> 1. Postman for testing endpoint
> 2. For Database we will use Mysql
> 3. Good interet connection to build project faster



## Tech Stack
- Java-17
- Spring Boot-3
- Spring Data JPA
- lombok
- MySQL
- Cloudinary
- Postman

## Modules
* Image Upload Module/ Transformation Image etc ....

## Installation & Run
Before running the API server, you should update the database config inside the application.properties file.
Update the port number, username and password as per your local database config and storage file path configuration.
    
```
# MySql Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/cloudinarydb
spring.datasource.username=root
spring.datasource.password=test
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# image size and path configuration
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# Cloudinary Credential Details Configuration
cloudinary.cloud.name=*** your cloud name ******
cloudinary.api.key=***** your api key ****
cloudinary.api.secret=**** your secret access key *****
```

## API Root Endpoint
```
https://localhost:8080/
user this data for checking purpose.
```

## Step To Be Followed
> 1. Create Rest Api will return to ImageData Details.
>    
>    **Project Documentation**
>    - **Entity** - ImageData (class)
>    - **Payload** - ApiResponse (class)
>    - **Repository** - ImageDataRepository (interface)
>    - **Service** - ImageService (interface), ImageServiceImpl (class)
>    - **Controller** - ImageController (Class)
>    - **Global Exception** - GlobalException(class)
>    - **Config** - CloudinaryConfig (class)
>
> 2. Add cloudinary-http44 dependency in pom.xml file.
> 3. configure Mysql configuration and Cloudinary accesskey, secret key,Cloud name in applcation.properties file
> 4. Create cloudinaryConfig class for verify cloudinary client Credentials.
> 5. Create Image service class to write Business logic to Upload image in Cloudinary Cloud and Transformation the image.
>      * Upload file to Cloudinary Cloud.
>      * Crop Image 
>      * Round The Corners image
>      * Circle Image
>      * Crop And Auto-Background Color Image
>      * Resizing And Cropping Image
>      * Gravity Auto Image
>      * Effects And Filters Image
> 6. Create Image Controller to use upload file and Transformation the image.
> 7. Create GlobalException class to handle all runtime exception.

## Important Dependency to be used
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
	<groupId>com.mysql</groupId>
	<artifactId>mysql-connector-j</artifactId>
	<scope>runtime</scope>
    </dependency>
    
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

<!-- https://mvnrepository.com/artifact/com.cloudinary/cloudinary-http44 -->
    <dependency>
         <groupId>com.cloudinary</groupId>
         <artifactId>cloudinary-http44</artifactId>
         <version>1.39.0</version>
    </dependency>
```

## configure Mysql configuration and Cloudinary accesskey, secret key,Cloud name in applcation.properties file.
```
# MySql Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/cloudinarydb
spring.datasource.username=root
spring.datasource.password=test
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# image size and path configuration
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB

# Cloudinary Credential Details Configuration
cloudinary.cloud.name=*** your cloud name ******
cloudinary.api.key=***** your api key ****
cloudinary.api.secret=**** your secret access key *****
```

## Create cloudinaryConfig class for verify cloudinary client Credentials.
```
@Configuration
public class CloudinaryConfig {

    // load cloud name
    @Value("${cloudinary.cloud.name}")
    private String cloud_name;

    // load api key
    @Value("${cloudinary.api.key}")
    private String api_key;

    // load api secret key
    @Value("${cloudinary.api.secret}")
    private String api_secret;


    // verify Cloudinary credential
    @Bean
    public Cloudinary getCloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloud_name,
                "api_key", api_key,
                "api_secret", api_secret,
                "secure", true));
        return cloudinary;
    }

}
```

## Create Image service class to write Business logic to Upload image in Cloudinary Cloud and Transformation the image.

### *ImageService*
```
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
```

### *ImageServiceImpl*
```
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

```

### Create Image Controller to use upload file and Transformation the image. 

```
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
```

### Following pictures will help to understand flow of API

### *PostMan Test Cases*

Url - http://localhost:8080/api/image/upload

![image](https://github.com/user-attachments/assets/9c701a2a-0d7f-4c60-b7ad-0ee31fb9fbdc)

URL: http://localhost:8080/api/image/create-crop-image/{id}

![image](https://github.com/user-attachments/assets/937b566f-638b-4543-a76b-7d08c76b0cab)

### Note: After the geting url copy and paste the browser search bar.

**Result of Transformation Image*
![image](https://github.com/user-attachments/assets/6f1af8b4-02ee-4459-9e9d-3543487ea447)

![image](https://github.com/user-attachments/assets/41c636f5-35d2-42dc-ba44-c4146e9f7309)

![image](https://github.com/user-attachments/assets/9145c305-6734-4c43-aa37-f7d1b729f841)

![image](https://github.com/user-attachments/assets/a27f4c8d-29b6-4cce-bcd4-ca9e7948fc71)

![image](https://github.com/user-attachments/assets/9329bf2e-1d23-4ac0-8ad2-3fa712da128a)

![image](https://github.com/user-attachments/assets/a49cb014-0b9f-48a0-814a-03835f883fa0)

![image](https://github.com/user-attachments/assets/38ebef8f-c916-4933-97c7-a87d6bf27222)

![image](https://github.com/user-attachments/assets/c9288f78-376a-4c9b-965d-47c362b0b473)


