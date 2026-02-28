package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class StopIndexer extends Command{
    private Indexer indexer;

    public StopIndexer(Indexer indexer){
        this.indexer = indexer;
        addRequirements(indexer);
    }

    @Override
    public void execute(){
        indexer.stopIndexer();
    }
    
    @Override
    public boolean isFinished(){
        return true;
    }
}
