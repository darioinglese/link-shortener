package com.farmu.challenge.application;

import com.farmu.challenge.adapter.persistance.LinkRepository;
import com.farmu.challenge.domain.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UnshortenLinkUseCase {
    private final LinkRepository linkRepository;

    public String execute(String shortlink) {
        var id = new String(Base64.decodeBase64(shortlink));
        log.info("id: {}", id);
        return linkRepository.findById(Long.parseLong(id))
                .map(Link::getLongLink)
                .orElseThrow(NoSuchElementException::new);
    }
}
