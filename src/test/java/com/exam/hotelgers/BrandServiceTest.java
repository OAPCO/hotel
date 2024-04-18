package com.exam.hotelgers;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.repository.BrandRepository;
import com.exam.hotelgers.service.BrandService;
import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Test
    void list_returnsPageOfBrands() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Brand brand = new Brand(); // 초기화에 필요한 속성 설정
        Page<Brand> expectedPage = new PageImpl<>(IntStream.range(0, 10).mapToObj(i -> brand).collect(Collectors.toList()));

        when(brandRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<BrandDTO> actualPage = brandService.list(pageable);

        // Assert
        assertEquals(expectedPage.getSize(), actualPage.getSize(), "The number of brands should match");
        verify(brandRepository, times(1)).findAll(pageable);
    }
}