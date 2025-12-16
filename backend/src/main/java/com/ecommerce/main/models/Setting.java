package com.ecommerce.main.models;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force=true)
@Table(name="settings")
public class Setting {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(name = "key_name", nullable=false, unique=true)
    private String key;

    @Column(name = "value_content", nullable=false, unique=false)
    private String value;

    @PostConstruct
    public void init() {
        System.out.println(">>> LOADING ENTITY SETTING");
    }
}
