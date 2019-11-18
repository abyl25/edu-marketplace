package com.seniorproject.educationplatform.dto.category;

import lombok.Data;

import java.util.List;

@Data
public class AddCategoryReq {
    private Long parentId;
    private List<String> subcategories;
}
