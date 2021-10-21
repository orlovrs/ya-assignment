public class Assignment {
    public static double getDeliveryPrice(double distance, boolean isBig, boolean isFragile, DeliveryLoad load) {
        if (isFragile && distance > 30)
            throw new IllegalArgumentException("Мы, конечно, довезем ваш груз, но он разобьется :)");

        double minDeliveryPrice = 400.0;
        double actualDeliveryPrice = (getDistanceTax(distance) +
                getSizeTax(isBig) +
                getFragileTax(isFragile)) *
                getLoadRate(load);

        return Math.max(minDeliveryPrice, actualDeliveryPrice);
    }

    private static double getDistanceTax(double distance) {
        if (distance <= 0) throw new IllegalArgumentException("Расстояние должно быть положительным");
        if (distance < 2) return 50;
        if (distance < 10) return 100;
        if (distance < 30) return 200;
        return 300;
    }

    private static double getSizeTax(boolean isBig) {
        return isBig ? 200 : 100;
    }

    private static double getFragileTax(boolean isFragile) {
        return isFragile ? 300 : 0;
    }

    private static double getLoadRate(DeliveryLoad load) {
        switch (load) {
            case UPPER: return 1.2;
            case HIGH: return 1.4;
            case CRITICAL: return 1.6;
            default: return 1;
        }
    }
}

enum DeliveryLoad {
    NORMAL, UPPER, HIGH, CRITICAL
}