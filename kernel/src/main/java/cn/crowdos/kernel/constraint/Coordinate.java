package cn.crowdos.kernel.constraint;

public class Coordinate implements Condition{
    final double longitude;
    final double latitude;
    public Coordinate(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Coordinate(){
        this(0,0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Coordinate){
            Coordinate coo = (Coordinate) obj;
            return (Double.compare(this.longitude, coo.longitude) == 0)
                    && (Double.compare(this.latitude, coo.latitude) == 0);
        }
        return false;
    }

    public boolean inLine(Coordinate other){
        return this.longitude == other.longitude || this.latitude == other.latitude;
    }

    public double euclideanDistance(Coordinate other){
        double dLon = longitude - other.longitude;
        double dLat = latitude - other.latitude;
        return dLon * dLon + dLat * dLat;
    }

    @Override
    public String toString() {
        return String.format("Coo<%.3f, %.3f>", longitude, latitude);
    }
}
