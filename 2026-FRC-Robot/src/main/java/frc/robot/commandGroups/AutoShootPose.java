// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandGroups;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import edu.wpi.first.math.geometry.Pose3d;
import java.util.function.DoubleSupplier;

import frc.robot.commandGroups.DriveWhileAim;
import frc.robot.commands.ShooterCommands.SpinShooter;
import frc.robot.commands.ShooterCommands.setHoodAngle;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterLogic;
import frc.robot.subsystems.drive.Drive;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoShootPose extends ParallelCommandGroup {
  /** Creates a new AutoShoot. */
  public AutoShootPose(ShooterLogic shooterLogic, Shooter shooter, Drive drive, Pose3d target, DoubleSupplier xSupplier, DoubleSupplier ySupplier) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    double[] compensatedComponents = shooterLogic.calculateShotChanges(target);



    addCommands(
      new SpinShooter(shooter, shooter.LinearSpeedToShooterPower(compensatedComponents[0])),
      new setHoodAngle(shooter, compensatedComponents[1]),
      new DriveWhileAim(drive, xSupplier, ySupplier, compensatedComponents[2])
    );
  }
}
