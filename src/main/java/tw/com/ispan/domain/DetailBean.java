// package tw.com.ispan.domain;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "detail")
// public class DetailBean {
// 	@Id
// 	@Column(name = "photoid")
// 	private Integer photoid;
	
// 	@Column(name = "photo", columnDefinition = "image")
// 	private byte[] photo;

// 	@Override
// 	public String toString() {
// 		return "DetailBean [photoid=" + photoid + "]";
// 	}
// 	public Integer getphotoid() {
// 		return photoid;
// 	}
// 	public void setPhotoid(Integer photoid) {
// 		this.photoid = photoid;
// 	}
// 	public byte[] getPhoto() {
// 		return photo;
// 	}
// 	public void setPhoto(byte[] photo) {
// 		this.photo = photo;
// 	}
// }
