// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ShooterCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterLogic;

import static frc.robot.Constants.ShooterConstants.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AimFlywheelSpeed extends Command {
  Shooter shooter;
  ShooterLogic logic;
  /** Creates a new SetShooterRPM. */
  public AimFlywheelSpeed(Shooter shooter, ShooterLogic logic) {
    this.shooter = shooter;
    this.logic = logic;
    addRequirements(shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = logic.getFlywheelExitVelocity(kHoodDefaultAngleRadians);
    SmartDashboard.putNumber("exit velocity",speed);
    shooter.setShooterVelocity(shooter.LinearSpeedToRPM(speed));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
