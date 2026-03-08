// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandGroups.Shooter;

import static frc.robot.Constants.IndexerConstants.kIndexerPower;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.SetIndexerPower;
import frc.robot.commands.ShooterCommands.SpinShooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Indexandshoot extends ParallelCommandGroup {
  Shooter shooter;
  Indexer indexer;
  /** Creates a new Indexandshoot. */
  public Indexandshoot( Shooter shooter,
  Indexer indexer) {
    this.indexer = indexer;
    this.shooter = shooter;

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new SetIndexerPower(indexer, kIndexerPower),
      new SpinShooter(shooter, 0.6)
    );
  }
}
