package dev.jeonghun.domain.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import dev.jeonghun.common.PageInfo;

public record PageDto<T>(
	List<T> data,
	PageInfo<T> pageInfo
) {
	public PageDto(Page<T> page) {
		this(page.getContent(), new PageInfo<>(page));
	}
}
