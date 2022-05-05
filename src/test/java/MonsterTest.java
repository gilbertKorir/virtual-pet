import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
@BeforeEach
public void setUp(){
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtual_pets_test", "postgres", "1234");  //Those with linux or windows use two strings for username and password
}
//empty persons and monsters
    @AfterEach
    public void afterseUp(){
        try(Connection con = DB.sql2o.open()) {
            String deleteMonstersQuery = "DELETE FROM monsters *;";
            con.createQuery(deleteMonstersQuery).executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
//

    @Test
    public void monster_instantiatesCorrectly_true() {
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(true, testMonster instanceof Monster);
    }
    @Test
    public void Monster_instantiatesWithName_String() {
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals("Bubbles", testMonster.getName());
    }
    @Test
    public void Monster_instantiatesWithPersonId_int() {
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(1, testMonster.getPersonId());
    }
    //Overiding
    @Test
    public void equals_returnsTrueIfNameAndPersonIdAreSame_true() {
        Monster testMonster = new Monster("Bubbles", 1);
        Monster anotherMonster = new Monster("Bubbles", 1);
        assertTrue(testMonster.equals(anotherMonster));
    }
    //save test
    @Test
    public void save_returnsTrueIfDescriptionsAretheSame() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.save();
        assertTrue(testMonster.equals(Monster.all().get(Monster.all().size()-1)));
    }

//    @Test
//    public void save_returnsTrueIfDescriptionsAretheSame() {
//        Monster testMonster = new Monster("Bubbles", 1);
//        testMonster.save();
//        assertTrue(Monster.all().get(0).equals(testMonster));
//    }
    //functionality of id and monster
    @Test
    public void save_assignsIdToMonster() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.save();
        Monster savedMonster = Monster.all().get(0);
        assertEquals(savedMonster.getId(), testMonster.getId());
    }
    //returning all database
    @Test
    public void all_returnsAllInstancesOfMonster_true() {
        Monster firstMonster = new Monster("Bubbles", 1);
        firstMonster.save();
        Monster secondMonster = new Monster("Spud", 1);
        secondMonster.save();
        assertEquals(true, Monster.all().get(0).equals(firstMonster));
        assertEquals(true, Monster.all().get(1).equals(secondMonster));
    }
    //finding monsters
    @Test
    public void find_returnsMonsterWithSameId_secondMonster() {
        Monster firstMonster = new Monster("Bubbles", 1);
        firstMonster.save();
        Monster secondMonster = new Monster("Spud", 3);
        secondMonster.save();
        assertEquals(Monster.find(secondMonster.getId()), secondMonster);
    }
    //one to many relationship
    @Test
    public void save_savesPersonIdIntoDB_true() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Monster testMonster = new Monster("Bubbles", testPerson.getId());
        testMonster.save();
        Monster savedMonster = Monster.find(testMonster.getId());
        assertEquals(savedMonster.getPersonId(), testPerson.getId());
    }
    //Updating person
    @Test
    public void getMonsters_retrievesAllMonstersFromDatabase_monstersList() {
        Person testPerson = new Person("Henry", "henry@henry.com");
        testPerson.save();
        Monster firstMonster = new Monster("Bubbles", testPerson.getId());
        firstMonster.save();
        Monster secondMonster = new Monster("Spud", testPerson.getId());
        secondMonster.save();
        Monster[] monsters = new Monster[] { firstMonster, secondMonster };
        assertTrue(testPerson.getMonsters().containsAll(Arrays.asList(monsters)));
    }
    //all new monster
    @Test
    public void monster_instantiatesWithHalfFullPlayLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL / 2));
    }
    @Test
    public void monster_instantiatesWithHalfFullSleepLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2));
    }
    @Test
    public void monster_instantiatesWithHalfFullFoodLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL / 2));
    }
    @Test
    public void isAlive_confirmsMonsterIsAliveIfAllLevelsAboveMinimum_true(){
        Monster testMonster = new Monster("Bubbles", 1);
        assertEquals(testMonster.isAlive(), true);
    }
    @Test
    public void depleteLevels_reducesAllLevels(){
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.depleteLevels();
        assertEquals(testMonster.getFoodLevel(), (Monster.MAX_FOOD_LEVEL / 2) - 1);
        assertEquals(testMonster.getSleepLevel(), (Monster.MAX_SLEEP_LEVEL / 2) - 1);
        assertEquals(testMonster.getPlayLevel(), (Monster.MAX_PLAY_LEVEL / 2) - 1);
    }
    //boolean test for alive
    @Test
    public void isAlive_recognizesMonsterIsDeadWhenLevelsReachMinimum_false(){
        Monster testMonster = new Monster("Bubbles", 1);
        for(int i = Monster.MIN_ALL_LEVELS; i <= Monster.MAX_FOOD_LEVEL; i++){
            testMonster.depleteLevels();
        }
        assertEquals(testMonster.isAlive(), false);
    }
    //interacting constants
    @Test
    public void play_increasesMonsterPlayLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.play();
        assertTrue(testMonster.getPlayLevel() > (Monster.MAX_PLAY_LEVEL / 2));
    }
    @Test
    public void sleep_increasesMonsterSleepLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.sleep();
        assertTrue(testMonster.getSleepLevel() > (Monster.MAX_SLEEP_LEVEL / 2));
    }
    @Test
    public void feed_increasesMonsterFoodLevel(){
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.feed();
        assertTrue(testMonster.getFoodLevel() > (Monster.MAX_FOOD_LEVEL / 2));
    }
    //anticipating errors
    @Test
    public void monster_foodLevelCannotGoBeyondMaxValue(){
        Monster testMonster = new Monster("Bubbles", 1);
        for(int i = Monster.MIN_ALL_LEVELS; i <= (Monster.MAX_FOOD_LEVEL); i++){
            try {
                testMonster.feed();
            } catch (UnsupportedOperationException exception){ }
        }
        assertTrue(testMonster.getFoodLevel() <= Monster.MAX_FOOD_LEVEL);
    }
    @Test
    public void save_recordsTimeOfCreationInDatabase() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.save();
        Timestamp savedMonsterBirthday = Monster.find(testMonster.getId()).getBirthday();
        Timestamp rightNow = new Timestamp(new Date().getTime());
//        assertEquals(rightNow, savedMonsterBirthday);
        assertEquals(rightNow.getDay(), savedMonsterBirthday.getDay());
    }
    @Test
    public void sleep_recordsTimeLastSleptInDatabase() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.save();
        testMonster.sleep();
        Timestamp savedMonsterLastSlept = Monster.find(testMonster.getId()).getLastSlept();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastSlept));
    }
    @Test
    public void play_recordsTimeLastPlayedInDatabase() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.save();
        testMonster.play();
        Timestamp savedMonsterLastPlayed = Monster.find(testMonster.getId()).getLastPlayed();
        Timestamp rightNow = new Timestamp(new Date().getTime());
        assertEquals(DateFormat.getDateTimeInstance().format(rightNow), DateFormat.getDateTimeInstance().format(savedMonsterLastPlayed));
    }
    @Test
    public void timer_executesDepleteLevelsMethod() {
        Monster testMonster = new Monster("Bubbles", 1);
        int firstPlayLevel = testMonster.getPlayLevel();
        testMonster.startTimer();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException exception){}
        int secondPlayLevel = testMonster.getPlayLevel();
        assertTrue(firstPlayLevel > secondPlayLevel);
    }
    @Test
    public void timer_haltsAfterMonsterDies() {
        Monster testMonster = new Monster("Bubbles", 1);
        testMonster.startTimer();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException exception){}
        assertFalse(testMonster.isAlive());
        assertTrue(testMonster.getFoodLevel() >= 0);
    }
}











