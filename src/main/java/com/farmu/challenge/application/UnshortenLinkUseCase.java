package com.farmu.challenge.application;

import com.farmu.challenge.adapter.persistance.LinkRepository;
import com.farmu.challenge.application.exception.LinkNotFoundException;
import com.farmu.challenge.config.ErrorCode;
import com.farmu.challenge.domain.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class UnshortenLinkUseCase {
    private final LinkRepository linkRepository;

    public String execute(String shortlink) {
        return linkRepository.findById(extractId(shortlink))
                .map(Link::getLongLink)
                .orElseThrow(() -> new LinkNotFoundException(ErrorCode.LINK_NOT_FOUND));
    }

    private long extractId(String shortLink) {
        try {
            return Long.parseLong(new String(Base64.decodeBase64(shortLink)));
        } catch (IllegalArgumentException e) {
            throw new LinkNotFoundException(ErrorCode.LINK_NOT_FOUND);
        }
    }
}
