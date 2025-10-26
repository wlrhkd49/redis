package com.development.practice.redis.service;

import com.development.practice.redis.entity.Board;
import com.development.practice.redis.repository.BoardRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // @Cacheable 어노테이션 : Cache Aside 전략으로 Caching 적용
    // cacheNames: 캐시 이름 설정
    // key: 캐시 키 설정 (page와 size를 조합하여 고유한 키 생성)
    // cacheManager: 사용할 캐시 매니저의 Bean 이름 지정
    @Cacheable(cacheNames = "getBoards",
            key = "'boards:page:' + #page + ':size:' + #size",
            cacheManager = "boardCacheManager")
    public List<Board> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> pageOfBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return pageOfBoards.getContent();
    }
}
