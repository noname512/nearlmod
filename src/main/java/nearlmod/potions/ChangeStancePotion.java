package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.cards.special.ToAtkStance;
import nearlmod.cards.special.ToDefStance;

import java.util.ArrayList;

public class ChangeStancePotion extends AbstractPotion {
    public static String ID = "nearlmod:ChangeStancePotion";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public ChangeStancePotion() {
        super(NAME, ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.ANCIENT);
        labOutlineColor = NLMOD.NearlGold;
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:defstance")), BaseMod.getKeywordDescription("nearlmod:defstance")));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:atkstance")), BaseMod.getKeywordDescription("nearlmod:atkstance")));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        ArrayList<AbstractCard> choice = new ArrayList<>();
        choice.add(new ToDefStance(potency));
        choice.add(new ToAtkStance(potency));
        addToBot(new ChooseOneAction(choice));
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ChangeStancePotion();
    }
}
