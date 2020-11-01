/** PA1
 * Raven O'Rourke & Lora Kalthoff
 *
 * Book class:
 * Inherits from Product class
 */
package edu.ucdenver.company;

import java.time.LocalDate;

public class Book extends Product{
    private String authorName;
    private LocalDate publicationDate;
    private int numOfPages;

    public Book(String name, String id, String brand, String description, LocalDate incorporatedDate,
                String authorName, LocalDate publicationDate, int numOfPages) {
        super(name, id, brand, description, incorporatedDate);
        this.authorName = authorName;
        this.publicationDate = publicationDate;
        this.numOfPages = numOfPages;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }

    @Override
    public String productType() {
        return "Book";
    }
}
