package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.IndexerConstants.*;

public class Indexer extends SubsystemBase {
    private SparkFlex indexerLeftMotor;
    private SparkFlex indexerRightMotor;
    

    public Indexer(){
        indexerLeftMotor = new SparkFlex(kLeftIndexerID, MotorType.kBrushless);
        indexerRightMotor = new SparkFlex(kRightIndexerID, MotorType.kBrushless);
    }

    public void setIndexerPower(double power){
        indexerLeftMotor.set(power);
        indexerRightMotor.set(-power);
    }

    public void setIndexerSpeed(double speed){
        indexerLeftMotor.getClosedLoopController().setSetpoint(speed, ControlType.kVelocity, null);
        indexerRightMotor.getClosedLoopController().setSetpoint(-speed, ControlType.kVelocity, null);
    }

     public double getIndexerSpeed(){
        return (indexerLeftMotor.getEncoder().getVelocity() -
        indexerRightMotor.getEncoder().getVelocity()) / 2;
    }

    public void stopIndexer(){
        setIndexerPower(0);
    }

    @Override    
    public void periodic() {
        SmartDashboard.putNumber("indexer speed", getIndexerSpeed());
    }
}
