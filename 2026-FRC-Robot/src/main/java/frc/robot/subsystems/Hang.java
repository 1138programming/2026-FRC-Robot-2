// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.HangConstants.*;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Hang extends SubsystemBase {
  /** Creates a new Hang. */

  private SparkFlex leftHangMotor;
  private SparkFlex rightHangMotor;


  SparkMaxConfig leaderConfig;
  SparkMaxConfig followerConfig;
  


  public Hang() {
    leftHangMotor = new SparkFlex(kLeftHangID, MotorType.kBrushless);
    rightHangMotor = new SparkFlex(kRightHangID, MotorType.kBrushless);

    


    leaderConfig = new SparkMaxConfig();
    leaderConfig
      .smartCurrentLimit(60)
      .idleMode(IdleMode.kCoast);

    followerConfig = new SparkMaxConfig();
    followerConfig
      .smartCurrentLimit(60)
      .idleMode(IdleMode.kCoast)
      .follow(leftHangMotor);




    leftHangMotor.configure(leaderConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightHangMotor.configure(followerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  }


  public void setHangPower(double power) {
    leftHangMotor.set(power);
  }

  public void stopHang() {
    leftHangMotor.set(0);
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
