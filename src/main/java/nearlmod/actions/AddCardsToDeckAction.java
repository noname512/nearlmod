package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class AddCardsToDeckAction extends AbstractGameAction {
    AbstractCard cardToObtain;
    int number;

    public AddCardsToDeckAction(AbstractCard card, int number) {
        this.cardToObtain = card;
        this.number = number;
        this.duration = 0.5F;
    }

    public void update() {
        for (int i = 0; i < number; i++) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.cardToObtain.makeStatEquivalentCopy(), (float)Settings.WIDTH / 2.0F + 175.0F * (i * 2 - number + 1), (float)Settings.HEIGHT / 2.0F));
        }
        this.isDone = true;
    }
}
