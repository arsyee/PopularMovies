package hu.fallen.popularmovies.adapters;

@SuppressWarnings({"WeakerAccess", "unused"}) // public access required for Data Binding and GSON
public class ReviewInfo {
    public String id;
    public String author;
    public String content;

    @Override
    public String toString() {
        return author;
    }
}
