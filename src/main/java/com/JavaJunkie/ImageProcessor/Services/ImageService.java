package com.JavaJunkie.ImageProcessor.Services;
import com.JavaJunkie.ImageProcessor.Entity.ImageEntity;
import com.JavaJunkie.ImageProcessor.Respository.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.io.image.ImageDataFactory;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class ImageService{
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public String uploadImage(MultipartFile file) throws IOException {
        ImageEntity image = new ImageEntity();
        image.setName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setImageData(file.getBytes());
        ImageEntity savedImage = imageRepository.save(image);
        return savedImage.getId();
    }
    public byte[] getImageById(String id){
        Optional<ImageEntity> imageOptional = imageRepository.findById(id);
        if (imageOptional.isPresent()) {
            return imageOptional.get().getImageData();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }
    }
    public ImageEntity getImageMetadataById(String id) {
        return imageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    public void deleteImage(String id){
         if(imageRepository.existsById(id)){
                imageRepository.deleteById(id);
                System.out.println("Image deleted Successfully!!!");
         }
         else{
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not found!!!");
         }
    }

    public ImageEntity updateImageData(String id, String newName, String newContentType){
     Optional<ImageEntity> imageEntityOptional =imageRepository.findById(id);
     if(imageEntityOptional.isPresent()){
         ImageEntity image=imageEntityOptional.get();
         image.setName(newName);
         image.setContentType(newContentType);
         return imageRepository.save(image);
     }
     else {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Image not Found!!!");
     }
    }

    public List<ImageEntity> getAllImages() {
        return imageRepository.findAll();
    }
    public boolean updateImageFile(String id, MultipartFile file) throws IOException {
          ImageEntity existingImage=imageRepository.findById(id).orElse(null);
          if(existingImage!=null){
              existingImage.setName(file.getOriginalFilename());
              existingImage.setContentType(file.getContentType());
              existingImage.setImageData(file.getBytes());
              imageRepository.save(existingImage);
              return true;
          }
          return false;
    }

    public ResponseEntity<byte[]> resizeImage(MultipartFile file, int width, int height) throws IOException {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive integers.");
        }

        InputStream inputStream = file.getInputStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(inputStream)
                .size(width, height)
                .outputFormat("png")
                .toOutputStream(outputStream);
        BufferedImage bufferedImage = Thumbnails.of(new ByteArrayInputStream(outputStream.toByteArray()))
                .size(width, height)
                .asBufferedImage();

        System.out.println("Resized width: " + bufferedImage.getWidth() + " , Resized height: " + bufferedImage.getHeight());

        byte[] resizedImageBytes = outputStream.toByteArray();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resizedImageBytes);
    }
    public byte[] rotateImage(MultipartFile file, double angle) throws IOException{
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = rotatedImage.createGraphics();
        graphics2D.rotate(Math.toRadians(angle), width / 2, height / 2);
        graphics2D.drawImage(originalImage, 0, 0, null);
        graphics2D.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, "PNG", baos);
        return baos.toByteArray();
}

public byte[] cropImage(MultipartFile file,int x,int y,int height,int width) throws IOException {
    BufferedImage originalImage = ImageIO.read(file.getInputStream());

    BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);

    System.out.println("Cropped Image Size: " + croppedImage.getWidth() + "x" + croppedImage.getHeight());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(croppedImage, "PNG", baos);
    return baos.toByteArray();
}

    public byte[] addWaterMark(MultipartFile file, String title) {
        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            Graphics2D graphics = (Graphics2D) originalImage.getGraphics();

            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("Lucida Handwriting", Font.ITALIC, 26);
            graphics.setFont(font);

            graphics.setColor(new Color(255, 255, 255, 150));

            int stringWidth = graphics.getFontMetrics().stringWidth(title);
            int stringHeight = graphics.getFontMetrics().getHeight();
            int x = originalImage.getWidth() - stringWidth - 20;
            int y = originalImage.getHeight() - stringHeight + 30;
            graphics.drawString(title, x, y);
            graphics.dispose();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to process the image", e);
        }

    }

    public byte[] convertToJPG(MultipartFile file) {
        try { String originalFilename = file.getOriginalFilename();
            String originalExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1) : "unknown";
//            logger.info("Original file extension: " + originalExtension);
//            System.out.println("Original file extension: " + originalExtension);

            BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ImageIO.write(inputImage, "jpg", outputStream);

            logger.info("Converted file extension: jpg");
            System.out.println("Converted file extension: jpg");

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image to JPG", e);
        }
    }

    public byte[] convertImageToPDF(MultipartFile file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        com.itextpdf.layout.element.Image pdfImage = new Image(com.itextpdf.io.image.ImageDataFactory.create(file.getBytes()));
        document.add(pdfImage);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] addGrayScaleFilter(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage grayscaleImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color c = new Color(originalImage.getRGB(x, y));
                int gray = (int) (c.getRed() * 0.299 + c.getGreen() * 0.587 + c.getBlue() * 0.114);
                Color grayColor = new Color(gray, gray, gray);
                grayscaleImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(grayscaleImage, "jpg", baos);
        baos.flush();
        return baos.toByteArray();
    }

    public byte[] addSepiaFilter(MultipartFile file)throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage sepiaImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < originalImage.getWidth(); x++) {
            for (int y = 0; y < originalImage.getHeight(); y++) {
                Color c = new Color(originalImage.getRGB(x, y));

                int tr = (int) (0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
                int tg = (int) (0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
                int tb = (int) (0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());

                tr = (tr > 255) ? 255 : tr;
                tg = (tg > 255) ? 255 : tg;
                tb = (tb > 255) ? 255 : tb;

                Color sepiaColor = new Color(tr, tg, tb);
                sepiaImage.setRGB(x, y, sepiaColor.getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(sepiaImage, "jpg", baos);
        baos.flush();
        return baos.toByteArray();
    }

    public byte[] mirrorImage(MultipartFile file, String direction) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage mirroredImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        if ("horizontal".equalsIgnoreCase(direction)) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    mirroredImage.setRGB(originalImage.getWidth() - 1 - x, y, originalImage.getRGB(x, y));
                }
            }
        } else if ("vertical".equalsIgnoreCase(direction)) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    mirroredImage.setRGB(x, originalImage.getHeight() - 1 - y, originalImage.getRGB(x, y));
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid direction. Use 'horizontal' or 'vertical'.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(mirroredImage, "jpg", baos);
        baos.flush();
        return baos.toByteArray();
    }

    public byte[] compressImage(MultipartFile file, float size) throws IOException{
        if (size < 0.0f || size > 1.0f) {
            throw new IllegalArgumentException("Quality must be between 0.0 and 1.0");
        }

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();

        JPEGImageWriteParam jpegWriteParam = new JPEGImageWriteParam(null);
        jpegWriteParam.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        jpegWriteParam.setCompressionQuality(size);
        writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));

        writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), jpegWriteParam);

        writer.dispose();

        return byteArrayOutputStream.toByteArray();
    }
}