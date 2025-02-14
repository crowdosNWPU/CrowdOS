package cn.crowdos.kernel.constraint;

class CoordinateTest {

    public static void main(String[] args) {
        testInLine();
        testToString();
    }

    public static void testInLine() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 0);
        Coordinate c3 = new Coordinate(0, 1);
        Coordinate c4 = new Coordinate(1, 0);
        Coordinate c5 = new Coordinate(1, 1);

        boolean result1 = c1.inLine(c2);
        boolean result2 = c1.inLine(c3);
        boolean result3 = c1.inLine(c4);
        boolean result4 = c1.inLine(c5);

        if (result1 && result2 && result3 && !result4) {
            System.out.println("testInLine passed");
        } else {
            System.out.println("testInLine failed");
        }
    }

    public static void testToString() {
        Coordinate c1 = new Coordinate(0, 0);
        System.out.println(c1);
    }
}