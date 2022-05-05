import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonTest {
    @BeforeEach
    public void setUp(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtual_pets_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
    }
//empty persons and monsters
    @AfterEach
    public void afterseUp(){
        try(Connection con = DB.sql2o.open()) {
            String deletePersonsQuery = "DELETE FROM persons *;";
            con.createQuery(deletePersonsQuery).executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
//
    @Test
    public void person_instantiatesCorrectly_true() {
        Person testPerson = new Person("Henry", "[email protected]");
        assertEquals(true, testPerson instanceof Person);

    }
    @Test
    public void getName_personIn_Henry(){
        Person testPerson = new Person("Henry", "[email protected]");
        assertEquals("Henry", testPerson.getName());
    }
    @Test
    public void getEmail_personIn_Henry(){
        Person testPerson = new Person("Henry", "[email protected]");
        assertEquals("[email protected]", testPerson.getEmail());
    }

    @Test
    public void save_insertsObjectIntoDatabase_Person() {
        Person testPerson = new Person("Henry", "[email protected] (Links to an external site.)");
        testPerson.save();
        assertTrue(Person.all().get(0).equals(testPerson));
    }
    @Test
    public void save_assignsIdToObject() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Person savedPerson = Person.all().get(0);
        assertEquals(testPerson.getId(), savedPerson.getId());
    }
    @Test
    public void all_returnsAllInstancesOfPerson_true() {
        Person firstPerson = new Person("Henry", "henry@henry.com");
        firstPerson.save();
        Person secondPerson = new Person("Harriet", "harriet@harriet.com");
        secondPerson.save();
        Person thirdPerson = new Person("Jared", "har@harriet.com");
        thirdPerson.save();
        assertEquals(true, Person.all().get(0).equals(firstPerson));
        assertEquals(true, Person.all().get(1).equals(secondPerson));
        assertEquals(true, Person.all().get(2).equals(thirdPerson));
    }
    @Test
    public void find_returnsPersonWithSameId_secondPerson() {
        Person firstPerson = new Person("Henry", "henry@henry.com");
        firstPerson.save();
        Person secondPerson = new Person("Harriet", "harriet@harriet.com");
        secondPerson.save();
        assertEquals(Person.find(secondPerson.getId()), secondPerson);
    }
}