package UDP;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 20251107L;

    private String id;
    private String title ;
    private String author ;
    private String isbn ;
    private String publishDate  ;

    public Book(String id, String title, String author, String isbn, String publishDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
    }

    public void fix(){
        String[] nameArr=this.title.trim().toLowerCase().split("\\s+");
        String namers="";
        for(int i=0;i<nameArr.length;i++){
            namers+= nameArr[i].substring(0,1).toUpperCase()+nameArr[i].substring(1);
            if(i!= nameArr.length-1) namers+=" ";
        }
        this.title=namers;

        String[] au=this.author.trim().toLowerCase().split("\\s+");
        String auname=au[0].toUpperCase()+",";
        for(int i=1;i<au.length;i++){
            auname+=" "+au[i].substring(0,1).toUpperCase()+au[i].substring(1);
        }
        this.author=auname;

        String[] datearr=this.publishDate.split("-");
        this.publishDate=datearr[1]+'/'+datearr[0];

        this.isbn=this.isbn.substring(0,3)+"-"+this.isbn.substring(3,4)+"-"+this.isbn.substring(4,6)+"-"+this.isbn.substring(6,12)+"-"+this.isbn.substring(12);
        System.out.println(this.title);
        System.out.println(this.author);
        System.out.println(this.publishDate);
        System.out.println(this.isbn);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
