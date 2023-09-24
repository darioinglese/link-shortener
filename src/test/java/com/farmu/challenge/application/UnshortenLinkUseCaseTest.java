package com.farmu.challenge.application;

import com.farmu.challenge.adapter.persistance.LinkRepository;
import com.farmu.challenge.application.exception.LinkNotFoundException;
import com.farmu.challenge.config.ErrorCode;
import com.farmu.challenge.domain.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unshorten Link UseCase")
class UnshortenLinkUseCaseTest {
    private final LinkRepository linkRepository = mock(LinkRepository.class);
    private final UnshortenLinkUseCase useCase = new UnshortenLinkUseCase(linkRepository);

    @Test
    void unshortenOk() {
        String shortLink = "MQ==";
        var storedLink = Link.builder()
                .id(1L)
                .longLink("http://www.google.com")
                .shortLink("MQ==")
                .build();
        when(linkRepository.findById(1L)).thenReturn(Optional.of(storedLink));

        var response = useCase.execute(shortLink);
        assertThat(response).isEqualTo(storedLink.getLongLink());
        verify(linkRepository, times(1)).findById(1L);
    }

    @Test
    void unshortenNotFound() {
        String shortLink = "MQ==";

        Throwable thrown = catchThrowable(() -> useCase.execute(shortLink));

        assertThat(thrown)
                .isInstanceOf(LinkNotFoundException.class)
                .hasMessage(ErrorCode.LINK_NOT_FOUND.getMessage());
        verify(linkRepository, times(1)).findById(1L);
    }

    @Test
    void unshortenInvalidShortlink() {
        String shortLink = "asd";

        Throwable thrown = catchThrowable(() -> useCase.execute(shortLink));

        assertThat(thrown)
                .isInstanceOf(LinkNotFoundException.class)
                .hasMessage(ErrorCode.LINK_NOT_FOUND.getMessage());
        verify(linkRepository, times(0)).findById(1L);
    }

}