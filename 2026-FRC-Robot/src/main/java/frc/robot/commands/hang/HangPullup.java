// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.hang;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Hang;
import static frc.robot.Constants.HangConstants.khighPower;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class HangPullup extends Command {
  private Hang hang;
  /** Creates a new HangPullup. */
  public HangPullup(Hang hang) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.hang = hang;
    addRequirements(hang);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    hang.setHangPower(khighPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    hang.stopHang();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
