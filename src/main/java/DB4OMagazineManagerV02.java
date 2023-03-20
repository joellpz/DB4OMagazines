import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import controller.FileController;
import model.Article;
import model.Author;
import model.Magazine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DB4OMagazineManagerV02 {
    private static ArrayList<Magazine> magazines;
    private static ObjectContainer db;

    public static void main(String[] args) throws IOException {
        DB4OMagazineManagerV02 MM = new DB4OMagazineManagerV02();
        // load ArrayList<Revista> data from files
        MM.loadMagazines();
        try {
            //open database represented by ObjectContainer
            System.out.println("*** Connect ***");
            MM.connect();
            // store magazines
            System.out.println("*** Store Magazines ***");
            MM.storeMagazines();
            // Querying magazines
            System.out.println("*** Get Magazines ***");
            MM.getMagazines();
            // Querying articles
            System.out.println("*** List Articles ***");
            MM.listArticles();
            // Querying authors
            System.out.println("*** Get Authors ***");
            MM.getAuthors();
            // Querying authors by author name
            System.out.println("*** Get author By Name ***");
            MM.getAuthorsByName("F. Perrier");
            // QUerying articles by author name
            System.out.println("*** Get Articles by Author By Name ***");
            MM.getArticlesByAuthorName("R. Manito");
            System.out.println("*** Delete Articles By Author Name ***");
            MM.deleteArticlesByAuthorName("R. Manito");
            System.out.println("*** Get Articles By Author Name ***");
            MM.getArticlesByAuthorName("R. Manito");
            System.out.println("*** Get German Authors ***");
            MM.getGermanAuthors();
            // Deleting author by id
            System.out.println("*** Delete Author by id ***");
            MM.deleteAuthor(15);
            // retrieveMagazine by id
            System.out.println("*** Get Magazine By id ***");
            MM.getMagazineContentByMagazineId(2);
            // deleting Magazine CASCADE by id
            System.out.println("*** Delete Magazine By id ***");
            MM.deleteMagazineContentByMagazineId(2);
            System.out.println("*** Get Magazine By id ***");
            MM.getMagazineContentByMagazineId(2);
            System.out.println("*** Get Author ***");
            MM.getAuthors();
            // Deleting all objects
            MM.clearDatabase();
            System.out.println("*** Get Magazines when clean ***");
            MM.getMagazines();

        } finally {
            db.close();
        }
    }

    // Method to connect and open database file
    public void connect() throws IOException {

        File file = new File("src/main/resources/revistesV02.db");
        String fullPath = file.getAbsolutePath();
        db = Db4o.openFile(fullPath);

        //	StringBuilder sb = new StringBuilder();
        //	var path = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
        //	path = Path.Combine(path, "t.db");
        //	File.Delete(path);

        //	var cf = Db4oEmbedded.NewConfiguration();
        //	cf.Common.Add(new TransparentActivationSupport());
        //	cf.Common.Add(new TransparentPersistenceSupport());

    }

    // Method to LOAD Magazines in memory from files using FileAccesor
    public void loadMagazines() throws IOException {
        FileController fileController = new FileController();
        fileController.readAuthorsFile("src/main/resources/autors.txt");
        fileController.readMagazinesFile("src/main/resources/revistes.txt");
        magazines = fileController.readArticlesFile("src/main/resources/articles.txt");
    }

    // Method to STORE all in memory magazines to the database
    public void storeMagazines() {
        System.out.println("\n\nRevistes llegides des del fitxer\n\nARA enmagatzemades a la BBDD");
        for (int i = 0; i < magazines.size(); i++) {
            System.out.println(magazines.get(i).toString());
            db.store(magazines.get(i));
        }
    }

    // Method to LIST all magazines from the database using QBE example
    public void getMagazines() {
        System.out.println("\n\nRevistes llegides des de la base de dades");
        ObjectSet<Magazine> result = db.queryByExample(new Magazine());
        System.out.println(result.size());
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }

    // Method to LIST all articles from the database using QBE example
    public void listArticles() {
        Article article = new Article();
        List<Article> articles = db.queryByExample(article);
        for (Article a : articles) {
            System.out.println(a);
        }
    }

    // Method to LIST all authors from the database using QBE example
    public void getAuthors() {
        Author author = new Author();
        List<Author> authors = db.queryByExample(author);
        for (Author a : authors) {
            System.out.println(a);
        }
    }

    // Method to QUERY articles by id_revista using QBE example
    public void getMagazineContentByMagazineId(int _id) {
        Magazine magazine = new Magazine(_id,null,null);
        List<Magazine> magazines = db.queryByExample(magazine);
        for (Magazine a : magazines) {
            System.out.println(a);
        }
    }

    // Method to DELETE all objects from the database using QBE example
    public void clearDatabase() {
        Object object = new Object();
        List<Object> magazines = db.queryByExample(object);
        for (Object a : magazines) {
            db.delete(a);
        }
    }

    // Method to DELETE revistes by id_revista using QBE example
    public void deleteMagazineContentByMagazineId(int _id) {
        Magazine magazine = new Magazine(_id,null,null);
        List<Magazine> magazines = db.queryByExample(magazine);
        for (Magazine a : magazines) {
            db.delete(a);
        }
    }

    // Method to DELETE an author from the database using QBE example
    public void deleteAuthor(int authorId) {
        ObjectSet<Author> result = db.queryByExample(new Author(authorId, null, null, null, true));
        while (result.hasNext()) {
            db.delete(result.next());
        }
    }

    // Method to QUERY authors by nacionalitat using Native Queries
    public void getGermanAuthors() {
        List<Author> authors = db.query(new Predicate<Author>() {
            public boolean match(Author author) {
                return author.isActive() && author.getNationality().compareTo("alemany") == 0;
            }
        });
    }

    // Method to QUERY authors by name using Native Queries
    public void getAuthorsByName(String _nom) {

    }

    // Method to QUERY articles by author's name using Native Queries
    public void getArticlesByAuthorName(String _nom) {

    }

    // Method to DELETE articles by author's name using Native Queries
    public void deleteArticlesByAuthorName(String _nom) {

    }
}	