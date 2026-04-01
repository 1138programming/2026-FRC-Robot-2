package frc.robot.commands.Indexer;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class SetIndexerPower extends Command {

    private Indexer indexer;
    private double power;

    public SetIndexerPower(Indexer indexer, double power){
        this.indexer = indexer;
        this.power = power;
        addRequirements(indexer);

    }

    @Override
    public void execute(){
        indexer.setIndexerPower(power);
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
