package com.mokuroku.backend.product.entity;

import com.mokuroku.backend.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Builder
public class Wishlist {

  @Id
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
