package models;

public class Movie {
	private int id;
	private String title;
	private String description;
	private String trailerPath;
	
	public Movie(int id, String title, String description, String trailerPath)
	{
		this.id = id;
		this.title = title;
		this.description = description;
		this.trailerPath = trailerPath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrailerPath() {
		return trailerPath;
	}

	public void setTrailerPath(String trailerPath) {
		this.trailerPath = trailerPath;
	}
	
}
