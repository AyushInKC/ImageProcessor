package com.JavaJunkie.ImageProcessor.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "images")
public class ImageEntity {
    @Id
    private String id;
    private String name;
    private String contentType;
    private byte[] imageData;
}
