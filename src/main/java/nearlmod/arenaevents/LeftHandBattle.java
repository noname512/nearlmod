package nearlmod.arenaevents;

import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.monsters.LeftHandTytusTopola;

public class LeftHandBattle extends AbstractImageEvent {
    public static final String ID = "nearlmod:LeftHandBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;
    public LeftHandBattle() {
        super(NAME, DESCRIPTIONS[0], "images/events/laughallyouwant.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy("Injury"));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                AbstractDungeon.lastCombatMetricKey = ID;
                (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMBAT;
                (AbstractDungeon.getCurrRoom()).monsters = new MonsterGroup(new LeftHandTytusTopola(0.0F, 0.0F));
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.imageEventText.clearRemainingOptions();
                this.imageEventText.setDialogOption(OPTIONS[2]);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Injury(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                openMap();
                return;
        }
        openMap();
    }
}
