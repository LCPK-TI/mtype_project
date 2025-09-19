package com.lcpk.mtype.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.lcpk.mtype.dto.BookDetailDto;
import com.lcpk.mtype.dto.BookListDto;
import com.lcpk.mtype.entity.BookEntity;
import com.lcpk.mtype.entity.CategoryEntity;
import com.lcpk.mtype.repository.BookRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final CategoryService categoryService;
	
	public Slice<BookListDto> getBookList(Long categoryNo, Pageable pageable) {
		if (categoryNo == null) {
			return bookRepository.findAllProjectedBy(pageable);
		} else {
			// CategoryService를 통해 자신 및 모든 하위 카테고리 No 목록을 가져오기
			List<Long> categoryNos = categoryService.findAllSubCategoryIds(categoryNo);

			// no 목록을 Repository에 전달하여 도서 조회
			return bookRepository.findByCategoryNosIn(categoryNos, pageable);
		}
	}

	public BookDetailDto getBookDetail(Long isbn) {
		// 도서 기본 정보
		BookEntity book = bookRepository.findById(isbn)
				.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + isbn));

		// 도서의 최상위 카테고리 조회
		CategoryEntity topCategory = categoryService.findTopParent(book.getCategory().getCategoryNo());


		return new BookDetailDto(book, topCategory);
	}
}
