package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class SetIndexerPower extends Command {

    private Indexer indexer;
    private double power;

    public SetIndexerPower(Indexer indexer, double power){
        this.indexer = indexer;
        this.power = power;
    }

    @Override
    public void execute(){
        indexer.setIndexerPower(power);
    }

    @Override
    public boolean isFinished(){
        return true;
    }


    
}
