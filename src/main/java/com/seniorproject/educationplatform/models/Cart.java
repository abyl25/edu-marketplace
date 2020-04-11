package com.seniorproject.educationplatform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart implements Serializable {
    @Id    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//  @JoinColumn(name = "userId")
    @ToString.Exclude
    @JsonIgnore
//    @JsonIgnoreProperties("cart")
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User student;

    @OneToMany(mappedBy = "cart", fetch=FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

}
