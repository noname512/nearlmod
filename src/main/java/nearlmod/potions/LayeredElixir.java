package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class LayeredElixir extends AbstractPotion {
    public static String ID = "nearlmod:LayeredElixir";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public LayeredElixir() {
        super(NAME, ID, PotionRarity.RARE, PotionSize.H, PotionColor.ELIXIR);
        this.labOutlineColor = NLMOD.NearlGold;
        this.description = potionStrings.DESCRIPTIONS[0];
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:stance")), BaseMod.getKeywordDescription("nearlmod:stance")));
    }
    @Override
    public void use(AbstractCreature abstractCreature) {
        AtkStance.upgradeIncNum(1);
        DefStance.upgradeIncNum(1);
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LayeredElixir();
    }
}
