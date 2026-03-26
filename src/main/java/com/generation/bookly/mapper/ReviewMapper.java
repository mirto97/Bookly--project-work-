package com.generation.bookly.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.bookly.dto.ReviewDTO;
import com.generation.bookly.model.Review;

@Mapper(componentModel = "spring", uses={BookingMapper.class})
public interface ReviewMapper 
{
    
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "booking.id", target = "bookingId")
    ReviewDTO toDTO(Review review);
    List<ReviewDTO> toDTO(List<Review> reviews);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "bookingId", target = "booking.id")
    Review toEntity(ReviewDTO reviewDTO);
    List<Review> toEntity(List<ReviewDTO> reviewDTOs);

}
