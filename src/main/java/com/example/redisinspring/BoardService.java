package com.example.redisinspring;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Cacheable(cacheNames = "getBoards", // 캐시 이름 설정
            key = "'boards:page:' + #page + ':size' + #size", // Redis에 저장할 key 이름 설정 ex) boards:page:1:size:10
    cacheManager = "boardCacheManager") // 사용할 cacheManager의 Bean 이름을 설정
    public List<Board> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> pageOfBoards = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        return pageOfBoards.getContent();
    }
}