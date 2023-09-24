package com.farmu.challenge.application;

import com.farmu.challenge.adapter.controller.model.LinkRequest;
import com.farmu.challenge.adapter.persistance.LinkRepository;
import com.farmu.challenge.domain.Link;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Shorten Link UseCase")
class ShortenLinkUseCaseTest {
    private final LinkRepository linkRepository = mock(LinkRepository.class);

    private final ShortenLinkUseCase useCase = new ShortenLinkUseCase(linkRepository);

    @Test
    void shortenLinkOk() {
        String basePath = "http://unit-test";
        String url = "http://www.google.com";
        String oneToBase64 = "MQ==";
        var request = new LinkRequest(url);
        when(linkRepository.save(any(Link.class))).thenReturn(Link.builder().id(1L).build());

        String result = useCase.execute(basePath, request);

        assertThat(result).isEqualTo(basePath.concat("/").concat(oneToBase64));
        verify(linkRepository, times(2)).save(any(Link.class));
    }

    @Test
    void shortenLinkAlredyExistsOk() {
        String basePath = "http://unit-test";
        String url = "http://www.google.com";
        String oneToBase64 = "MQ==";
        Link linkk = Link.builder()
                .id(1L)
                .shortLink(oneToBase64)
                .longLink(url)
                .build();
        var request = new LinkRequest(url);
        when(linkRepository.findByLongLink(url))
                .thenReturn(Optional.of(linkk));
        String result = useCase.execute(basePath, request);

        assertThat(result).isEqualTo(basePath.concat("/").concat(oneToBase64));
        verify(linkRepository, times(1)).findByLongLink(url);
        verify(linkRepository, times(0)).save(any(Link.class));
    }
}