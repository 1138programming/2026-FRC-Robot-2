package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.IndexerConstants.*;

public class Indexer extends SubsystemBase {
    private SparkFlex indexerMotorLeader;
    private SparkFlex indexerMotorFollower;

    private SparkMaxConfig leaderConfig;
    private SparkMaxConfig followerConfig;

    public Indexer(){
        
        indexerMotorLeader = new SparkFlex(kIndexerLeaderID, MotorType.kBrushless);
        indexerMotorFollower = new SparkFlex(kIndexerFollowerID, MotorType.kBrushless);


        leaderConfig = new SparkMaxConfig();
        leaderConfig
            .smartCurrentLimit(60)
            .idleMode(IdleMode.kCoast);

        followerConfig = new SparkMaxConfig();
        followerConfig
            .smartCurrentLimit(60)
            .idleMode(IdleMode.kCoast)
            .follow(indexerMotorLeader);

        indexerMotorLeader.configure(leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        indexerMotorFollower.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        

    }

    public void setIndexerPower(double power){
        indexerMotorLeader.set(power);
    }

    public void setIndexerSpeed(double speed){
        indexerMotorLeader.getClosedLoopController().setSetpoint(speed, ControlType.kVelocity, null);
    }

     public double getIndexerSpeed(){
        return indexerMotorLeader.getEncoder().getVelocity();
    }

    public void stopIndexer(){
        setIndexerPower(0);
    }

    @Override    
    public void periodic() {
        SmartDashboard.putNumber("indexer speed", getIndexerSpeed());
    }
}
