// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandGroups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.Drive;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveWhileAim extends SequentialCommandGroup {
  /** Creates a new DriveWhileAim. */

  public DriveWhileAim(Drive drive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, Pose2d target) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    //determine angle to aim at based on the target pose2d
    Translation2d diffTranslation = target.getTranslation().minus(drive.getPose().getTranslation());
    addCommands(DriveCommands.joystickDriveAtAngle(drive, xSupplier, ySupplier, () -> diffTranslation.getAngle()));

  }

  public DriveWhileAim(Drive drive, DoubleSupplier xSupplier, DoubleSupplier ySupplier, double angle) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(DriveCommands.joystickDriveAtAngle(drive, xSupplier, ySupplier, () -> new Rotation2d(angle)));

   }
}
