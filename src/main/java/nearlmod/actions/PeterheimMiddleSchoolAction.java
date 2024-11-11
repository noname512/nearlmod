package nearlmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class PeterheimMiddleSchoolAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("nearlmod:Peterheim");
    public static final String[] TEXT = uiStrings.TEXT;
    public final AbstractPlayer p = AbstractDungeon.player;
    public PeterheimMiddleSchoolAction(int amount) {
        this.actionType = ActionType.SPECIAL;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            if (p.hand.group.size() <= amount) {
                for (AbstractCard c : p.hand.group) {
                    c.retain = true;
                    c.superFlash();
                }
                isDone = true;
                return;
            }
            duration -= Gdx.graphics.getDeltaTime();
            AbstractDungeon.handCardSelectScreen.open(TEXT[0] + amount + TEXT[1], amount, false, false, false, false);
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                c.retain = true;
                c.superFlash();
                p.hand.addToTop(c);
            }
            p.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }
    }
}
