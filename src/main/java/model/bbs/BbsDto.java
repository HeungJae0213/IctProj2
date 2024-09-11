package model.bbs;
import java.sql.Blob;
import java.sql.Date;

public class BbsDto {
	private long id;
	private String username;
	private String title;
	private String content;
	private String files;
	private long hitcount;
    private Date postDate;
    private String thumbnail;
    
	//생성자]
    public BbsDto() {}
	public BbsDto(long id, String username, String title, String content, String files, long hitcount,
			Date postDate, String thumbnail) {
		this.id = id;
		this.username = username;
		this.title = title;
		this.content = content;
		this.files = files;
		this.hitcount = hitcount;
		this.postDate = postDate;
		this.thumbnail = thumbnail;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFiles() {
		return files;
	}
	public void setFiles(String string) {
		this.files = string;
	}
	public long getHitcount() {
		return hitcount;
	}
	public void setHitcount(long hitcount) {
		this.hitcount = hitcount;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
}
