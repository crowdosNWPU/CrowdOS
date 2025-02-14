package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// 为了让该类能在不同包中被访问，声明为 public
class TemporalPointTest {

    TemporalPoint temporalPoint;

    public TemporalPointTest() {
        try {
            // 尝试创建 TemporalPoint 对象
            temporalPoint = new TemporalPoint("10:00:00", 10);
        } catch (Exception e) {
            // 若创建失败，打印异常信息
            System.err.println("Failed to create TemporalPoint instance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void decomposer() {
        if (temporalPoint != null) {
            try {
                // 获取分解器
                Decomposer<Constraint> decomposer = temporalPoint.decomposer();
                // 打印平凡分解结果
                System.out.println(decomposer.trivialDecompose());
                // 打印按比例分解结果
                System.out.println(decomposer.scaleDecompose(10));
            } catch (DecomposeException e) {
                // 若分解过程出现异常，打印异常信息
                System.err.println("decomposer() test failed: " + e.getMessage());
            }
        } else {
            // 若 temporalPoint 为 null，提示未初始化
            System.err.println("TemporalPoint is not initialized, cannot run decomposer test.");
        }
    }

    public void satisfy() {
        if (temporalPoint != null) {
            // 使用正确的日期格式 HH:mm:ss
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
            try {
                // 解析时间并创建 DateCondition 对象
                DateCondition time1 = new DateCondition(sf.parse("10:30:00").getTime());
                DateCondition time2 = new DateCondition(sf.parse("10:05:00").getTime());
                if (temporalPoint.satisfy(time1)) {
                    System.out.println("satisfy(time1) test failed");
                } else {
                    System.out.println("satisfy(time1) test passed");
                }
                if (temporalPoint.satisfy(time2)) {
                    System.out.println("satisfy(time2) test passed");
                } else {
                    System.out.println("satisfy(time2) test failed");
                }
            } catch (ParseException e) {
                // 若解析时间出现异常，打印异常信息
                System.err.println("satisfy() test failed: " + e.getMessage());
            }
        } else {
            // 若 temporalPoint 为 null，提示未初始化
            System.err.println("TemporalPoint is not initialized, cannot run satisfy test.");
        }
    }

    public void getConditionClass() {
        if (temporalPoint != null) {
            if (temporalPoint.getConditionClass() == DateCondition.class) {
                System.out.println("getConditionClass() test passed");
            } else {
                System.out.println("getConditionClass() test failed");
            }
        } else {
            // 若 temporalPoint 为 null，提示未初始化
            System.err.println("TemporalPoint is not initialized, cannot run getConditionClass test.");
        }
    }

    public static void main(String[] args) {
        TemporalPointTest test = new TemporalPointTest();
        test.decomposer();
        test.satisfy();
        test.getConditionClass();
    }
}