package frc.robot.commands.auto;

import frc.robot.subsystems.DriveSystem;
import frc.robot.subsystems.FieldPositioningSystem;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoIntakeDown extends SequentialCommandGroup {
    public AutoIntakeDown(DriveSystem driveSystem, FieldPositioningSystem fieldPositioningSystem) {
        addCommands(
            new MoveXInchesBackwards(driveSystem, fieldPositioningSystem, 100, .5),
            new Wait(.5),
            new MoveXInchesForward(driveSystem, fieldPositioningSystem, 100, .45)

        );
    }

}
