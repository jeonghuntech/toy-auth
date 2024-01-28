package dev.jeonghun.common;

import org.springframework.data.domain.Page;

public record PageInfo<T>(
	long count,                // 결과 수
	long perPage,            // 요청 크기
	long totalCount,        // 전체 수

	long page,                // 요청 페이지
	long totalPage            // 전체 페이지 수
) {
	public PageInfo(Page<T> page) {
		this(
			page.getNumberOfElements(),
			page.getSize(),
			page.getTotalElements(),
			page.getNumber() + 1,
			page.getTotalPages()
		);
	}
}
