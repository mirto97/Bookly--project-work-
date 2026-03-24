package com.generation.bookly.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses={BookingMapper.class})
public interface ReviewMapper {
    
    ReviewDTO toDTO(Review review);
    List<ReviewDTO> toDTO(List<Review> reviews);

    Review toEntity(ReviewDTO reviewDTO);
    List<Review> toEntity(List<ReviewDTO> reviewDTOs);

}
