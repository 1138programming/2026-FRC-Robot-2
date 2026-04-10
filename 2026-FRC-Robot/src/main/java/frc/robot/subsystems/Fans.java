package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import static frc.robot.Constants.FanConstants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Fans extends SubsystemBase
{
    private SparkMax farLeftFanMotor;
    private SparkMax midLeftFanMotor;
    private SparkMax midRightFanMotor;
    private SparkMax farRightFanMotor;

    private SparkMaxConfig globalConfig;

    public Fans ()
    {
        farLeftFanMotor = new SparkMax(kfarLeftFanID, MotorType.kBrushless);
        midLeftFanMotor = new SparkMax(kMidLeftFanID, MotorType.kBrushless);
        midRightFanMotor = new SparkMax(kMidRightFanID, MotorType.kBrushless);
        farRightFanMotor = new SparkMax(kFarRightFanID, MotorType.kBrushless);

        // globalConfig = new SparkMaxConfig();
        // globalConfig
        //     .smartCurrentLimit(60)
        //     .idleMode(IdleMode.kBrake);

        // farLeftFanMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // midLeftFanMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // midRightFanMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // farRightFanMotor.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    @Override 
    public void periodic() {
        SmartDashboard.putNumber("left fan vel", farLeftFanMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("left mid fan vel", midLeftFanMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("right mid fan vel", midRightFanMotor.getEncoder().getVelocity());
        SmartDashboard.putNumber("right fan vel", farRightFanMotor.getEncoder().getVelocity());
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

    //individual methods
    public void setfarLeftFanMotorPower(double power)
    {
        farLeftFanMotor.set(power);
    }

    public void setmidLeftFanMotorPower(double power)
    {
        midLeftFanMotor.set(power);
    }

    public void setmidRightFanMotorPower(double power)
    {
        midRightFanMotor.set(power);
    }

    public void setfarRightFanMotorPower(double power)
    {
        farRightFanMotor.set(power);
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