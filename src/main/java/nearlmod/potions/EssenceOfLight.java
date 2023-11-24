package nearlmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import nearlmod.NLMOD;
import nearlmod.cards.LightCard;
import nearlmod.powers.LightPower;

public class EssenceOfLight extends AbstractPotion {
    public static String ID = "nearlmod:EssenceOfLight";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public EssenceOfLight() {
        super(NAME, ID, PotionRarity.COMMON, PotionSize.ANVIL, PotionColor.ENERGY);
        this.labOutlineColor = NLMOD.NearlGold;
        this.description = potionStrings.DESCRIPTIONS[0];
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("nearlmod:light")), BaseMod.getKeywordDescription("nearlmod:light")));
    }
    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, 10)));
        addToBot(new MakeTempCardInHandAction(new LightCard()));
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new EssenceOfLight();
    }
}
