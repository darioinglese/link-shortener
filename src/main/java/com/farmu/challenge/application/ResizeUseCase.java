package com.farmu.challenge.application;

import com.farmu.challenge.adapter.controller.model.ImageRequest;
import com.farmu.challenge.adapter.persistance.ImageRepository;
import com.farmu.challenge.application.exception.ImageProcessingException;
import com.farmu.challenge.config.ErrorCode;
import com.farmu.challenge.domain.Img;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResizeUseCase {

    private final ImageRepository imageRepository;

    public String execute(ImageRequest request) {
        String[] parts = request.image().split(",");
        if(parts.length != 2) throw new ImageProcessingException(ErrorCode.IMAGE_DECODING_EXCEPTION);
        String mimetype = parts[0];
        String extension = mimetype.substring(mimetype.indexOf('/') + 1, mimetype.indexOf(';'));
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(parts[1]);
        } catch (IllegalArgumentException e) {
            throw new ImageProcessingException(ErrorCode.IMAGE_DECODING_EXCEPTION);
        }
        String result;
        try {
            result = resize(bytes, request.x(), request.y(), extension);
        } catch (IOException e) {
            log.error("couldn't read image format");
            throw new ImageProcessingException(ErrorCode.IMAGE_PROCESSING_EXCEPTION);
        }
        log.info(">> saving image to db");
        var response = mimetype.concat(",").concat(result);
        imageRepository.save(Img.builder().original(request.image()).resized(response).build());
        return response;
    }

    private String resize(byte[] bytes, int x, int y, String extension) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        Image resized = image.getScaledInstance(x, y, Image.SCALE_DEFAULT);
        ImageIO.write(toBufferedImage(resized), extension, baos);
        baos.toByteArray();
        baos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private BufferedImage toBufferedImage(Image img) {
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
}
