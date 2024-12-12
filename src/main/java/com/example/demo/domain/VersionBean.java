package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Version")
public class VersionBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="version")
	private String version;
	
	@JsonIgnoreProperties("versionBean")
	@OneToMany(mappedBy = "versionBean")
	private List<MovieVersionBean> movieVersionBeans = new ArrayList<>();
	
	// 无参构造函数（Hibernate 需要）
    public VersionBean() {}

    // 全参构造函数
    public VersionBean(Integer id, String version) {
        this.id = id;
        this.version = version;
    }
}
