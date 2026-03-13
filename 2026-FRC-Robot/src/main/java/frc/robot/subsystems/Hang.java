// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.HangConstants.*;

public class Hang extends SubsystemBase {
  /** Creates a new Hang. */

  private TalonFX hangMotor;

  public Hang() {
    hangMotor = new TalonFX(kHangID);
    configureHangMotor();
  }

  private void configureHangMotor() {
    final TalonFXConfiguration config = new TalonFXConfiguration()
      .withMotorOutput(
          new MotorOutputConfigs()
            .withInverted(InvertedValue.Clockwise_Positive)
            .withNeutralMode(NeutralModeValue.Brake)
      );

      hangMotor.getConfigurator().apply(config);
  }

  public void setHangPower(double power) {
    hangMotor.set(power);
  }

  public void stopHang() {
    hangMotor.set(0);
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
