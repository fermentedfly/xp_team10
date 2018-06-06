package at.tugraz.xp10;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import at.tugraz.xp10.model.ShoppingList;
import at.tugraz.xp10.util.ListEntry;

import static junit.framework.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ListEntry.class})

public class ListEntryUnitTest {

    @Test
    public void listEntryTest(){
        ShoppingList sl = new ShoppingList();
        ListEntry list = new ListEntry("SOMETHING", sl);
        assertEquals(list.id, "SOMETHING");
        assertEquals(list.shoppingList, sl);
    }
}
