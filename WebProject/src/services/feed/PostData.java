package services.feed;

public class PostData {
	private String pictureLocation;
	private String picture;
	private String description;

	public PostData() {
		super();
	}

	public PostData(String pictureLocation, String picture, String description) {
		super();
		this.pictureLocation = pictureLocation;
		this.picture = picture;
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPictureLocation() {
		return pictureLocation;
	}

	public void setPictureLocation(String pictureLocation) {
		this.pictureLocation = pictureLocation;
	}

}
