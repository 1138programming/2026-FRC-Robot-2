package frc.robot.commands.Fans;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Fans;

public class SetOutsideFansPower extends Command{

    private Fans fans;
    private double power;

    public SetOutsideFansPower(Fans fans, double power)
    {
        this.fans = fans;
        this.power = power;
        addRequirements(fans);
    }

    @Override
    public void execute()
    {
        fans.setfarLeftFanMotorPower(power);
        fans.setfarRightFanMotorPower(power);
    }

     @Override
    public void end(boolean interrupted){
        fans.stopAllFans();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}