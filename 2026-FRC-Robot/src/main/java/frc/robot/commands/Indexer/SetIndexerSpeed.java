package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class SetIndexerSpeed extends Command {

    private Indexer indexer;
    private double speed;

    public SetIndexerSpeed(Indexer indexer, double speed){
        this.indexer = indexer;
        this.speed = speed;
        addRequirements(indexer);

    }

    @Override
    public void execute(){
        indexer.setIndexerSpeed(speed);
    }

      @Override
    public void end(boolean interrupted){
        indexer.stopIndexer();
    }


    @Override
    public boolean isFinished(){
        return false;
    }
}
