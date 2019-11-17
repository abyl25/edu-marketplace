package com.seniorproject.educationplatform.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
    @MapsId
    private User student;

    @OneToMany(mappedBy = "cart", fetch=FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

}
