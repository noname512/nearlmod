package nearlmod.arenaevents;
import com.megacrit.cardcrawl.events.AbstractImageEvent;

public abstract class AbstractArenaEvent extends AbstractImageEvent {
    public CurScreen screen = CurScreen.INTRO;
    public enum CurScreen {
        INTRO, FIGHT, LEAVE
    }
    public AbstractArenaEvent(String name, String description, String imgUrl) {
        super(name, description, imgUrl);
    }
}
