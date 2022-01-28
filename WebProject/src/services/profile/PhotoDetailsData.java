package services.profile;

import java.time.LocalDateTime;

public class PhotoDetailsData {
	private String id;
	private String author;
	private String pictureLocation;
	private String description;
	private LocalDateTime posted;

	public PhotoDetailsData(String id, String author, String pictureLocation, String description,
			LocalDateTime posted) {
		super();
		this.id = id;
		this.author = author;
		this.pictureLocation = pictureLocation;
		this.description = description;
		this.posted = posted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPictureLocation() {
		return pictureLocation;
	}

	public void setPictureLocation(String pictureLocation) {
		this.pictureLocation = pictureLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getPosted() {
		return posted;
	}

	public void setPosted(LocalDateTime posted) {
		this.posted = posted;
	}

}
