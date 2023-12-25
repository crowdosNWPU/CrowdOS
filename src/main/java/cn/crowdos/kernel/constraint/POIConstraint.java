package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;

import java.util.Collections;
import java.util.List;

public class POIConstraint implements Constraint{
    private final Coordinate location;
    private final double satisfyRadius;

    public POIConstraint(Coordinate location) {
        this(location, 10);
    }

    public POIConstraint(Coordinate location, double satisfyRadius){
        this.location = location;
        this.satisfyRadius = satisfyRadius;
    }

    public Coordinate getLocation() {
        return location;
    }


    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                return Collections.singletonList(new POIConstraint(location));
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                throw new DecomposeException("unsupported");
            }
        };
    }


    @Override
    public boolean satisfy(Condition condition) {
        if (! ( condition instanceof Coordinate)) return false;
        Coordinate participantLocation = (Coordinate) condition;
        return participantLocation.euclideanDistance(location) <= satisfyRadius * satisfyRadius;
    }


    @Override
    public Class<? extends Condition> getConditionClass() {
        return Coordinate.class;
    }


    @Override
    public String description() {
        return "POI Constraint";
    }
}
