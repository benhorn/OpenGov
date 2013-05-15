package au.gov.nsw.records.digitalarchive.search;

public class Book {
	private final String title;
	private final String author;
	private final String published;
	private final String category;
 
	public Book(final String title, final String author,
			final String published, final String category) {
		this.title = title;
		this.author = author;
		this.published = published;
		this.category = category;
	}
 
	public String getTitle() {
		return title;
	}
 
	public String getAuthor() {
		return author;
	}
 
	public String getPublished() {
		return published;
	}
 
	public String getCategory() {
		return category;
	}
}