package com.lcpk.mtype.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lcpk.mtype.dto.BookListDto;
import com.lcpk.mtype.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

	// 전체 도서 가져오기
	Slice<BookListDto> findAllProjectedBy(Pageable pageable);

	// 해당 카테고리번호리스트에 해당하는 도서 가져오기
	@Query("SELECT new com.lcpk.mtype.dto.BookListDto(b.isbn, b.title,b.publisher, b.price, b.imgUrl) "
			+ "FROM BookEntity b "
			+ "WHERE b.category.categoryNo IN :categoryNos")
	Slice<BookListDto> findByCategoryNosIn(@Param("categoryNos") List<Long> categoryNos, Pageable pageable);
}
