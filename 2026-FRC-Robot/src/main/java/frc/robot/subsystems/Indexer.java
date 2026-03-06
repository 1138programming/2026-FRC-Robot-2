package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.IndexerConstants.*;

public class Indexer extends SubsystemBase {
    private SparkFlex indexerMotor;
    

    public Indexer(){
        indexerMotor = new SparkFlex(kIndexerID, MotorType.kBrushless);
    }

    public void setIndexerPower(double power){
        indexerMotor.set(power);
    }

    public void setIndexerSpeed(double speed){
        indexerMotor.getClosedLoopController().setSetpoint(speed, ControlType.kVelocity, null);
    }

     public double getIndexerSpeed(){
        return indexerMotor.getEncoder().getVelocity();
    }

    public void stopIndexer(){
        setIndexerPower(0);
    }

    @Override    
    public void periodic() {
        SmartDashboard.putNumber("indexer speed", getIndexerSpeed());
    }
}
