package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.orbs.AbstractFriend;

public class FriendshipDrink extends AbstractPotion {
    public static String ID = "nearlmod:FriendshipDrink";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public FriendshipDrink() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.M, PotionColor.POWER);
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
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:friend")), BaseMod.getKeywordDescription("nearlmod:friend")));
        tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:trust")), BaseMod.getKeywordDescription("nearlmod:trust")));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof AbstractFriend)
                ((AbstractFriend) orb).applyStrength(potency);
    }

    @Override
    public int getPotency(int i) {
        return 2;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FriendshipDrink();
    }
}
