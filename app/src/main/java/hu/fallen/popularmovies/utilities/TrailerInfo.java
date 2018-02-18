package hu.fallen.popularmovies.utilities;

@SuppressWarnings({"WeakerAccess", "unused"}) // public access required for Data Binding and GSON
public class TrailerInfo {
    public String id;
    public String key;
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
