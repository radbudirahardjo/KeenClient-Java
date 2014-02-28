package io.keen.client.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests the FileEventSTore class.
 *
 * @author Kevin Litwack (kevin@kevinlitwack.com)
 * @since 2.0.0
 */
public abstract class EventStoreTestBase {

    protected static final String TEST_EVENT_1 = "{\"param1\":\"value1\"}";
    protected static final String TEST_EVENT_2 = "{\"param2\":\"value2\"}";
    protected static final String TEST_EVENT_3 = "{\"param3\":\"value3\"}";
    protected static final String TEST_EVENT_4 = "{\"param4\":\"value4\"}";
    protected static final String TEST_EVENT_5 = "{\"param5\":\"value5\"}";

    protected abstract KeenEventStore buildStore() throws IOException;

    protected KeenEventStore store;

    @Before
    public void createStore() throws Exception {
        store = buildStore();
    }

    @After
    public void disposeStore() {
        store = null;
    }

    @Test
    public void storeAndGetEvent() throws Exception {
        Object handle = store.store("collection1", TEST_EVENT_1);
        String retrieved = store.get(handle);
        assertEquals(TEST_EVENT_1, retrieved);
    }

    @Test
    public void removeEvent() throws Exception {
        Object handle = store.store("collection1", TEST_EVENT_1);
        store.remove(handle);
        String retrieved = store.get(handle);
        assertNull(retrieved);
    }

    @Test
    public void removeHandleTwice() throws Exception {
        Object handle = store.store("collection1", TEST_EVENT_1);
        store.remove(handle);
        store.remove(handle);
    }

    @Test
    public void getHandles() throws Exception {
        // Add a couple events to the store.
        store.store("collection1", TEST_EVENT_1);
        store.store("collection2", TEST_EVENT_2);

        // Get the handle map.
        Map<String, List<Object>> handleMap = store.getHandles();
        assertNotNull(handleMap);
        assertEquals(2, handleMap.size());

        // Get the lists of handles.
        List<Object> handles1 = handleMap.get("collection1");
        assertNotNull(handles1);
        assertEquals(1, handles1.size());
        List<Object> handles2 = handleMap.get("collection2");
        assertNotNull(handles2);
        assertEquals(1, handles2.size());

        // Validate the actual events.
        assertEquals(TEST_EVENT_1, store.get(handles1.get(0)));
        assertEquals(TEST_EVENT_2, store.get(handles2.get(0)));
    }

}
