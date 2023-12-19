package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.actions.ChooseSpecificCardAction;
import nearlmod.characters.Nearl;

import java.util.ArrayList;
import java.util.Collections;

public class FriendPotion extends AbstractPotion {
    public static String ID = "nearlmod:FriendPotion";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public FriendPotion() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.CARD, PotionColor.ANCIENT);
        labOutlineColor = NLMOD.NearlGold;
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic("SacredBark")) {
            description = potionStrings.DESCRIPTIONS[0];
        } else {
            description = potionStrings.DESCRIPTIONS[1];
        }
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:friendcard")), BaseMod.getKeywordDescription("nearlmod:friendcard")));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        ArrayList<AbstractCard> list = Nearl.getUnuniqueFriendCard(false);
        Collections.shuffle(list);
        ArrayList<AbstractCard> chooseList = new ArrayList<>(list.subList(0, 3));
        addToBot(new ChooseSpecificCardAction(chooseList, true, potency));
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FriendPotion();
    }
}
