package services.profile;

public class AdminDeleteData {
	private String postId;
	private String text;

	public AdminDeleteData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminDeleteData(String postId, String text) {
		super();
		this.postId = postId;
		this.text = text;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
