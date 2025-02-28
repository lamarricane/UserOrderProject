package com.example.model;

import com.example.view.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id", nullable = false)
    @JsonView(Views.UserDetails.class)
    private long id;

    @Column(name = "article_numbers", columnDefinition = "bigint[]")
    @JsonView(Views.UserDetails.class)
    private Set<Long> articleNumbers;

    @Column(name = "amount", nullable = false)
    @JsonView(Views.UserDetails.class)
    private BigDecimal amount;

    @Column(name = "order_date")
    @CreationTimestamp
    @JsonView(Views.UserDetails.class)
    private LocalDate orderDate;

    @Column(name = "status", nullable = false)
    @JsonView(Views.UserDetails.class)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.UserDetails.class)
    @JsonBackReference
    private User user;
}
