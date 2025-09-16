package com.mokuroku.backend.product.entity;

import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Wishlist {

  @Id
  @Column(name = "wishlist_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long wishlistId;

  @ManyToOne
  @JoinColumn(name = "email")
  private Member email;

  private String name;
  private String description;
  private String nationCode;
  private LocalDateTime regDate;

  @OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Product> products = new ArrayList<>();
}
