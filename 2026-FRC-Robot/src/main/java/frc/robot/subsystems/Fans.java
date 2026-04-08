package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import static frc.robot.Constants.FanConstants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Fans extends SubsystemBase
{
    private SparkMax farLeftFanMotor;
    private SparkMax midLeftFanMotor;
    private SparkMax midRightFanMotor;
    private SparkMax farRightFanMotor;

    public Fans ()
    {
        farLeftFanMotor = new SparkMax(kfarLeftFanID, MotorType.kBrushless);
        midLeftFanMotor = new SparkMax(kMidLeftFanID, MotorType.kBrushless);
        midRightFanMotor = new SparkMax(kMidRightFanID, MotorType.kBrushless);
        farRightFanMotor = new SparkMax(kFarRightFanID, MotorType.kBrushless);
    }

    public void stopAllFans()
    {
        farLeftFanMotor.set(0);
        midLeftFanMotor.set(0);
        midRightFanMotor.set(0);
        farRightFanMotor.set(0);
    }

    public void stopRightFans()
    {
        midRightFanMotor.set(0);
        farRightFanMotor.set(0);
    }

    public void stopLeftFans()
    {
        farLeftFanMotor.set(0);
        midLeftFanMotor.set(0);
    }

    public void setAllFanPower (double power)
    {
        farLeftFanMotor.set(power);
        midLeftFanMotor.set(power);
        midRightFanMotor.set(power);
        farRightFanMotor.set(power);
    }

    public void setRightFanPower(double power)
    {
        midRightFanMotor.set(power);
        farRightFanMotor.set(power);
    }

    public void setLeftFanPower(double power)
    {
        farLeftFanMotor.set(power);
        midLeftFanMotor.set(power);
    }

    public double getAverageFanSpeed()
    {
        return (farLeftFanMotor.getEncoder().getVelocity()
        + midLeftFanMotor.getEncoder().getVelocity()
        + midRightFanMotor.getEncoder().getVelocity()
        + farRightFanMotor.getEncoder().getVelocity())/4;
    }

    public double getAverageRightFanSpeed()
    {
        return (midRightFanMotor.getEncoder().getVelocity()
        + farRightFanMotor.getEncoder().getVelocity())/2;
    }

    public double getAverageLeftFanSpeed()
    {
        return (farLeftFanMotor.getEncoder().getVelocity()
        + midLeftFanMotor.getEncoder().getVelocity())/2;
    }
}