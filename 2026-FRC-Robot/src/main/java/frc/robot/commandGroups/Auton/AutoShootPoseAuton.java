// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandGroups.Auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import edu.wpi.first.math.geometry.Pose3d;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import frc.robot.commands.DriveCommands;
import frc.robot.commands.Auton.AimFlywheelPoseAndIndex;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterLogic;
import frc.robot.subsystems.drive.Drive;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoShootPoseAuton extends ParallelCommandGroup {
  /** Creates a new AutoShoot. */
  public AutoShootPoseAuton(ShooterLogic shooterLogic, Shooter shooter, Indexer indexer, Drive drive, Supplier<Pose3d> target, DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    addCommands(
      DriveCommands.joystickDriveAtAngle(drive, xSupplier, ySupplier, () -> shooterLogic.botAngletoPose2d(target.get().toPose2d())),
      new AimFlywheelPoseAndIndex(shooter,indexer,shooterLogic,target));
    
  }
}







