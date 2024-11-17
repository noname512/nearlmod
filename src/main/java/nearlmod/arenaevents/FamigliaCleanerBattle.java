package nearlmod.arenaevents;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.monsters.FamigliaCleaner;
import nearlmod.monsters.LeftHandTytusTopola;
import nearlmod.relics.Revenge;

import java.util.ArrayList;

public class FamigliaCleanerBattle extends AbstractArenaEvent {
    public static final String ID = "nearlmod:FamigliaCleanerBattle";
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    public FamigliaCleanerBattle() {
        super(NAME, DESCRIPTIONS[0], "resources/nearlmod/images/events/lefthandbattle.png");
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1], CardLibrary.getCopy("Injury"), new Revenge());
        noCardsInRewards = true;
    }

    public static ArrayList<AbstractCard> getCardsWithRarity(AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        AbstractPlayer p = AbstractDungeon.player;
        int numCards = 3;
        for (AbstractRelic r : p.relics)
            numCards = r.changeNumberOfCardsInReward(numCards);
        if (ModHelper.isModEnabled("Binary")) numCards--;
        AbstractCard card = null;
        for (int i = 0; i < numCards; i++) {
            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;
                if (p.hasRelic("PrismaticShard")) {
                    card = CardLibrary.getAnyColorCard(rarity);
                } else {
                    card = AbstractDungeon.getCard(rarity);
                }
                for (AbstractCard c : list)
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        break;
                    }
            }
            if (card != null) {
                list.add(card);
            }
        }
        return list;
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (screen != CurScreen.INTRO) {
            openMap();
            return;
        }
        switch (buttonPressed) {
            case 0:
                logMetric(ID, "Fight");
                CardCrawlGame.music.fadeOutTempBGM();
                CardCrawlGame.music.playTempBgmInstantly("m_bat_warchaos_combine.mp3", true);
                screen = CurScreen.FIGHT;
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractNearlCard.addSpecificCardsToReward(getCardsWithRarity(AbstractCard.CardRarity.UNCOMMON));
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                AbstractDungeon.getCurrRoom().addGoldToRewards(50);
                AbstractDungeon.lastCombatMetricKey = ID;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters = new MonsterGroup(new FamigliaCleaner(0.0F, 0.0F));
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                enterCombatFromImage();
                return;
            case 1:
                logMetric(ID, "Leave");
                screen = CurScreen.LEAVE;
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Injury(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new Revenge());
                openMap();
                return;
        }
        openMap();
    }
}
