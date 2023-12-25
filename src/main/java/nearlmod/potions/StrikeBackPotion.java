package nearlmod.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.powers.StormCounterAttackPower;

public class StrikeBackPotion extends AbstractPotion {
    public static String ID = "nearlmod:StrikeBackPotion";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public StrikeBackPotion() {
        super(NAME, ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.BLUE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.BLOCK.NAMES[0]), GameDictionary.keywords.get(GameDictionary.BLOCK.NAMES[0])));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new StormCounterAttackPower(p, potency)));
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new StrikeBackPotion();
    }
}
