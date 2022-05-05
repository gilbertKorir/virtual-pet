import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule  {

    @BeforeEach
    public void setUp(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtual_pets_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }
//    protected void before() {
//        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtual_pets_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
//    }
//empty persons and monsters
    @AfterEach
    public void afterseUp(){
        try(Connection con = DB.sql2o.open()) {
            String deletePersonsQuery = "DELETE FROM persons *;";
            String deleteMonstersQuery = "DELETE FROM monsters *;";
            con.createQuery(deletePersonsQuery).executeUpdate();
            con.createQuery(deleteMonstersQuery).executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
//    @Override
//    protected void after() {
//        try(Connection con = DB.sql2o.open()) {
//            String deletePersonsQuery = "DELETE FROM persons *;";
//            String deleteMonstersQuery = "DELETE FROM monsters *;";
//            con.createQuery(deletePersonsQuery).executeUpdate();
//            con.createQuery(deleteMonstersQuery).executeUpdate();
//        }
//    }

}








