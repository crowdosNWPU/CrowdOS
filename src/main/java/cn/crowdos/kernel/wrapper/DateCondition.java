package cn.crowdos.kernel.wrapper;

import cn.crowdos.kernel.constraint.Condition;

import java.util.Date;

public class DateCondition extends Date implements Condition {
    public DateCondition(){
        super();
    }
    public DateCondition(long date){
        super(date);
    }

    public DateCondition(String date){
        super(date);
    }

}
