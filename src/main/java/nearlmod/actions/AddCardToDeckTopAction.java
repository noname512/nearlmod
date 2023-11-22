package nearlmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddCardToDeckTopAction extends AbstractGameAction {

    private final AbstractCard card;

    public AddCardToDeckTopAction(AbstractCreature source, AbstractCard card) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.source = source;
        this.card = card;
    }

    @Override
    public void update() {
        (AbstractDungeon.getCurrRoom()).souls.onToDeck(card, false);
        isDone = true;
    }
}
