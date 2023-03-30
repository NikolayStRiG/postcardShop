package com.example.postcardshop.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageDto<T> {

  private final List<T> content;
  private final int number;
  private final int size;
  private final int numberOfElements;
  private final int totalPages;
  private final long totalElements;
  private final boolean first;
  private final boolean last;

  public static <P> PageDto<P> of(Page<P> page) {
    return new PageDto<>(page);
  }

  private PageDto(Page<T> page) {
    this.content = page.getContent();
    this.number = page.getNumber();
    this.size = page.getSize();
    this.numberOfElements = page.getNumberOfElements();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
    this.first = page.isFirst();
    this.last = page.isLast();
  }

}
