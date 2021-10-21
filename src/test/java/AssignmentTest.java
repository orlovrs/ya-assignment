import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssignmentTest {

    @ParameterizedTest
    @MethodSource("distancesFragile")
    void checkDistanceTax(double distance, boolean isBig, boolean isFragile, DeliveryLoad load, double expected) {
        double price = Assignment.getDeliveryPrice(distance, isBig, isFragile, load);
        Assertions.assertEquals(expected, price, "Ожидали цену " + expected + ", получили - " + price);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1.0})
    void checkNegativeOrNullDistance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Assignment.getDeliveryPrice(0, false, false, DeliveryLoad.NORMAL);
        });
        assertEquals("Расстояние должно быть положительным", exception.getMessage());
    }

    @Test
    void checkFragileIsNotForLongDistance() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Assignment.getDeliveryPrice(30.1, false, true, DeliveryLoad.NORMAL);
        });
        assertEquals("Мы, конечно, довезем ваш груз, но он разобьется :)", exception.getMessage());
    }

    private static Stream<Arguments> distancesFragile() {
        return Stream.of(
                // Distance component testing
                Arguments.of(1.0, true, true, DeliveryLoad.NORMAL, 550.0),
                Arguments.of(2.0, true, true, DeliveryLoad.NORMAL, 600.0),
                Arguments.of(9.0, true, true, DeliveryLoad.NORMAL, 600.0),
                Arguments.of(10.0, true, true, DeliveryLoad.NORMAL, 700.0),
                Arguments.of(29.0, true, true, DeliveryLoad.NORMAL, 700.0),
                Arguments.of(30.0, true, false, DeliveryLoad.NORMAL, 500.0),
                // Measures component testing
                Arguments.of(9.0, true, true, DeliveryLoad.NORMAL, 600),
                Arguments.of(9.0, false, true, DeliveryLoad.NORMAL, 500),
                // Fragile component testing
                Arguments.of(29.0, true, true, DeliveryLoad.NORMAL, 700),
                Arguments.of(29.0, true, false, DeliveryLoad.NORMAL, 400),
                // Load rate component testing
                Arguments.of(29.0, true, false, DeliveryLoad.NORMAL, 400),
                Arguments.of(29.0, true, false, DeliveryLoad.UPPER, 480),
                Arguments.of(29.0, true, false, DeliveryLoad.HIGH, 560),
                Arguments.of(29.0, true, false, DeliveryLoad.CRITICAL, 640),
                // Minimal price for delivery
                Arguments.of(1.0, false, false, DeliveryLoad.NORMAL, 400)
        );
    }
}
