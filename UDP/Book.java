package UDP;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 20251107L;

    private String id, title, author, isbn, publishDate;

    public Book(String id, String title, String author, String isbn, String publishDate) {
        this.id = id; this.title = title; this.author = author; this.isbn = isbn; this.publishDate = publishDate;
    }

    public String getId()          { return id; }
    public String getTitle()       { return title; }
    public String getAuthor()      { return author; }
    public String getIsbn()        { return isbn; }
    public String getPublishDate() { return publishDate; }

    public void setTitle(String s)       { this.title = s; }
    public void setAuthor(String s)      { this.author = s; }
    public void setIsbn(String s)        { this.isbn = s; }
    public void setPublishDate(String s) { this.publishDate = s; }

    // === fix() y như anh mô tả ===
    public void fix(){
        String[] nameArr = this.title.trim().toLowerCase().split("\\s+");
        String namers = "";
        for (int i = 0; i < nameArr.length; i++) {
            namers += nameArr[i].substring(0,1).toUpperCase() + nameArr[i].substring(1);
            if (i != nameArr.length - 1) namers += " ";
        }
        this.title = namers;

        String[] au = this.author.trim().toLowerCase().split("\\s+");
        String auname = au[0].toUpperCase() + ",";
        for (int i = 1; i < au.length; i++) {
            auname += " " + au[i].substring(0,1).toUpperCase() + au[i].substring(1);
        }
        this.author = auname;

        String[] datearr = this.publishDate.split("-");
        this.publishDate = datearr[1] + '/' + datearr[0];

        this.isbn = this.isbn.substring(0,3) + "-" + this.isbn.substring(3,4) + "-" +
                    this.isbn.substring(4,6) + "-" + this.isbn.substring(6,12) + "-" +
                    this.isbn.substring(12);

        // In ra kiểm tra (tuỳ chọn)
        System.out.println(this.title);
        System.out.println(this.author);
        System.out.println(this.publishDate);
        System.out.println(this.isbn);
    }

    @Override
    public String toString() {
        return "Book{id='%s', title='%s', author='%s', isbn='%s', publishDate='%s'}"
                .formatted(id, title, author, isbn, publishDate);
    }
}
