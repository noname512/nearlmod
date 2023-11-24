package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
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
import nearlmod.powers.LightPower;

public class FriendshipDrink extends AbstractPotion {
    public static String ID = "nearlmod:FriendshipDrink";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public FriendshipDrink() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.M, PotionColor.POWER);
        this.labOutlineColor = NLMOD.NearlGold;
        this.description = potionStrings.DESCRIPTIONS[0];
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:friend")), BaseMod.getKeywordDescription("nearlmod:friend")));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:trust")), BaseMod.getKeywordDescription("nearlmod:trust")));
    }
    @Override
    public void use(AbstractCreature abstractCreature) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof AbstractFriend)
                ((AbstractFriend) orb).applyStrength(1);
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FriendshipDrink();
    }
}
