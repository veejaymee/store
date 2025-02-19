package com.securitease.store.mapper;

import com.securitease.store.entity.Product;
import com.securitease.store.model.ProductDTO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);
}
