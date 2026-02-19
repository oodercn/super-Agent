package net.ooder.sdk.service.network.route;

import net.ooder.sdk.service.network.route.model.Route;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

public class ShortestPathCalculatorTest {

    private ShortestPathCalculator calculator;

    @Before
    public void setUp() {
        calculator = new ShortestPathCalculator();
    }

    @Test
    public void testCalculateDirectRoute() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route result = calculator.calculate("A", "B", routes);

        assertNotNull(result);
        assertEquals("A", result.getSourceId());
        assertEquals("B", result.getDestinationId());
        assertEquals(2, result.getHops().size());
        assertEquals("A", result.getHops().get(0));
        assertEquals("B", result.getHops().get(1));
    }

    @Test
    public void testCalculateMultiHopRoute() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route routeBtoC = new Route();
        routeBtoC.setSourceId("B");
        routeBtoC.setDestinationId("C");
        routes.add(routeBtoC);

        Route result = calculator.calculate("A", "C", routes);

        assertNotNull(result);
        assertEquals("A", result.getSourceId());
        assertEquals("C", result.getDestinationId());
        assertEquals(3, result.getHops().size());
        assertTrue(result.getHops().contains("A"));
        assertTrue(result.getHops().contains("B"));
        assertTrue(result.getHops().contains("C"));
    }

    @Test
    public void testCalculateNoRoute() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route result = calculator.calculate("A", "C", routes);

        assertNull(result);
    }

    @Test
    public void testCalculateSameSourceAndDestination() {
        Collection<Route> routes = new ArrayList<>();

        Route result = calculator.calculate("A", "A", routes);

        assertNull(result);
    }

    @Test
    public void testCalculateAlternativesSingle() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route routeBtoC = new Route();
        routeBtoC.setSourceId("B");
        routeBtoC.setDestinationId("C");
        routes.add(routeBtoC);

        Route routeAtoC = new Route();
        routeAtoC.setSourceId("A");
        routeAtoC.setDestinationId("C");
        routes.add(routeAtoC);

        List<Route> alternatives = calculator.calculateAlternatives("A", "C", 1);

        assertNotNull(alternatives);
    }

    @Test
    public void testCalculateAlternativesMultiple() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route routeBtoC = new Route();
        routeBtoC.setSourceId("B");
        routeBtoC.setDestinationId("C");
        routes.add(routeBtoC);

        Route routeAtoC = new Route();
        routeAtoC.setSourceId("A");
        routeAtoC.setDestinationId("C");
        routes.add(routeAtoC);

        List<Route> alternatives = calculator.calculateAlternatives("A", "C", 2);

        assertNotNull(alternatives);
    }

    @Test
    public void testCalculateAlternativesZeroMax() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        List<Route> alternatives = calculator.calculateAlternatives("A", "B", 0);

        assertNotNull(alternatives);
        assertTrue(alternatives.isEmpty());
    }

    @Test
    public void testCalculateAlternativesNegativeMax() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        List<Route> alternatives = calculator.calculateAlternatives("A", "B", -1);

        assertNotNull(alternatives);
        assertTrue(alternatives.isEmpty());
    }

    @Test
    public void testRouteStatus() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route result = calculator.calculate("A", "B", routes);

        assertNotNull(result);
        assertEquals("active", result.getStatus());
        assertTrue(result.isActive());
    }

    @Test
    public void testHopCount() {
        Collection<Route> routes = new ArrayList<>();

        Route routeAtoB = new Route();
        routeAtoB.setSourceId("A");
        routeAtoB.setDestinationId("B");
        routes.add(routeAtoB);

        Route routeBtoC = new Route();
        routeBtoC.setSourceId("B");
        routeBtoC.setDestinationId("C");
        routes.add(routeBtoC);

        Route result = calculator.calculate("A", "C", routes);

        assertNotNull(result);
        assertEquals(2, result.getHopCount());
    }
}
