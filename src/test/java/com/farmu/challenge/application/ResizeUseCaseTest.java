package com.farmu.challenge.application;

import com.farmu.challenge.adapter.controller.model.ImageRequest;
import com.farmu.challenge.adapter.persistance.ImageRepository;
import com.farmu.challenge.application.exception.ImageProcessingException;
import com.farmu.challenge.config.ErrorCode;
import com.farmu.challenge.domain.Img;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Resize UseCase")
class ResizeUseCaseTest {
    private final ImageRepository imageRepository = mock(ImageRepository.class);
    private final ResizeUseCase useCase = new ResizeUseCase(imageRepository);

    private final static String base64big = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAd0lEQVR4XmNggIIXPrb/YRgmhgFeu9tKIivEqeHW7Vv/FRQU4ApA7E+fPmEqBEnCwBcojdXEZ8+eQSRzEsEYBEBiIAxXBNIJE4RpwFCETSEyxlAIogP9Av6DsI+3L5gGiYHYMIyiCQZgElB/gQG6GjCwsDbDkAAA3HWh/oxNDGYAAAAASUVORK5CYII=";
    private final static String base64small = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJklEQVR4XmN47W4r+cLH9j8IM4AAiAECcIFnz579h2GwADIAqQQAS/4eVK/1kz8AAAAASUVORK5CYII=";

    @Test
    void resizeOk() {
        var result = useCase.execute(new ImageRequest(base64big, 4, 4));

        assertThat(result).isEqualTo(base64small);
        verify(imageRepository, times(1)).save(Img.builder()
                .original(base64big)
                .resized(base64small)
                .build());
    }

    @Test
    void resizeInvalidImage() {
        Throwable thrown = catchThrowable(() -> useCase.execute(new ImageRequest("data:image/png;base64,invalidFormat", 4, 4)));

        assertThat(thrown)
                .isInstanceOf(ImageProcessingException.class)
                .hasMessage(ErrorCode.IMAGE_DECODING_EXCEPTION.getMessage());

        verify(imageRepository, times(0)).save(any(Img.class));
    }

    @Test
    void resizeInvalidBase64Format() {
        Throwable thrown = catchThrowable(() -> useCase.execute(new ImageRequest("invalidFormat", 4, 4)));

        assertThat(thrown)
                .isInstanceOf(ImageProcessingException.class)
                .hasMessage(ErrorCode.IMAGE_DECODING_EXCEPTION.getMessage());

        verify(imageRepository, times(0)).save(any(Img.class));
    }



}