package at.tugraz.xp10;

import org.junit.Test;

import java.util.HashMap;

import at.tugraz.xp10.model.Category;
import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.model.ShoppingListItem;
import at.tugraz.xp10.model.User;
import at.tugraz.xp10.util.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModelUnitTest {

    boolean called = false;

    @Test
    public void testCategory() {
        Category c = new Category();
        c.setName("foo");
        assertEquals("foo", c.getName());

        c = new Category("bar");
        assertEquals("bar", c.getName());
    }

    @Test
    public void testShoppingList() {
        ShoppingList s = new ShoppingList();
        s.setTitle("test_title");
        assertEquals("test_title", s.getTitle());

        s.setDescription("test_description");
        assertEquals("test_description", s.getDescription());

        HashMap<String, String> members = new HashMap<>();
        members.put("ID1", "member1");
        members.put("ID2", "member2");
        members.put("ID3", "member3");

        s.setMembers(members);
        assertEquals(members, s.getMembers());

        HashMap<String, Boolean> items = new HashMap<>();
        items.put("item1", true);
        items.put("item2", false);
        items.put("item3", true);

        s.setItems(items);
        assertEquals(items, s.getItems());

        s = new ShoppingList("title", "description", "ownerID");
        assertEquals(true, s.getItems().isEmpty());
        assertEquals("title", s.getTitle());
        assertEquals("description", s.getDescription());
        assertEquals(false, s.getMembers().isEmpty());
        assertEquals(Constants.OWNER, s.getMembers().get("ownerID"));

        assertNotNull(s.getCreatedAt());
    }

    @Test
    public void testShoppingListItem()
    {
        ShoppingListItem s = new ShoppingListItem();
        s.setName("test");
        assertEquals("test", s.getName());

        s.setCategory("category");
        assertEquals("category", s.getCategory());

        s.setIsPurchased(true);
        assertEquals(true, s.getIsPurchased());

        s.setQuantity(5.5);
        assertEquals(5.5, s.getQuantity(), 0.000001);

        s.setUnit("foo");
        assertEquals("foo", s.getUnit());

        s.setTempId("temp");
        assertEquals("temp", s.getTempId());

        s = new ShoppingListItem("name", 10., "kg", "category", true, "tempId");
        assertEquals("name", s.getName());
        assertEquals(10., s.getQuantity(), 0.0001);
        assertEquals("kg", s.getUnit());
        assertEquals("category", s.getCategory());
        assertEquals(true, s.getIsPurchased());
        assertEquals("tempId", s.getTempId());
    }

    @Test
    public void testUser()
    {
        User u = new User();
        u.seteMail("foo@bar.com");
        assertEquals("foo@bar.com", u.geteMail());

        u.setEmailNotifications(true);
        assertEquals(true, u.getEmailNotifications());

        u.setFirstName("foo");
        assertEquals("foo", u.getFirstName());

        u.setLastName("bar");
        assertEquals("bar", u.getLastName());

        assertEquals("foo bar", u.getName());

        HashMap<String, String> friends = new HashMap<>();
        friends.put("ID1", "friend1");
        friends.put("ID2", "friend2");
        friends.put("ID3", "friend3");
        u.setFriends(friends);
        assertEquals(friends, u.getFriends());

        HashMap<String, Boolean> shoppinglists = new HashMap<>();
        shoppinglists.put("ID1", true);
        shoppinglists.put("ID2", false);
        shoppinglists.put("ID3", true);

        u.setShoppinglists(shoppinglists);
        assertEquals(shoppinglists, u.getShoppinglists());

        User other = new User("bar@baz.at", "first", "last");
        assertEquals("bar@baz.at", other.geteMail());
        assertEquals("first", other.getFirstName());
        assertEquals("last", other.getLastName());
        assertEquals(true, other.getEmailNotifications());
        assertEquals(true, other.getShoppinglists().isEmpty());
        assertEquals(true, other.getFriends().isEmpty());

        called = false;
        u.setUserListener(new User.UserListener() {
            @Override
            public void onLoaded(User user) {
                called = true;
            }
        });
        u.setData(other);
        assertEquals(other.geteMail(), u.geteMail());
        assertEquals(other.getFirstName(), u.getFirstName());
        assertEquals(other.getLastName(), u.getLastName());
        assertEquals(other.getShoppinglists(), u.getShoppinglists());
        assertEquals(true, called);

        u.addFriend("friend");
        assertEquals(Constants.FRIEND_REQUEST_ISSUED, u.getFriends().get("friend"));

        u.addFriendRequest("friend2");
        assertEquals(Constants.FRIEND_REQUEST_PENDING, u.getFriends().get("friend2"));

        u.confirmFriend("friend2");
        assertEquals(Constants.FRIENDS_REQUEST_CONFIRMED, u.getFriends().get("friend2"));

        u.removeFriend("friend");
        assertEquals(false, u.getFriends().containsKey("friend"));
    }
}
