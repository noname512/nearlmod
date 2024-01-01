package nearlmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.powers.MyFadingPower;

public class InvigoratingPotion extends AbstractPotion {
    public static String ID = "nearlmod:InvigoratingPotion";
    public static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public InvigoratingPotion() {
        super(NAME, ID, PotionRarity.RARE, PotionSize.EYE, PotionEffect.RAINBOW, Color.WHITE, null, null);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SacredBark")) {
            description = potionStrings.DESCRIPTIONS[1];
        } else {
            description = potionStrings.DESCRIPTIONS[0];
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 15 * potency)));
        addToBot(new ApplyPowerAction(p, p, new BufferPower(p, 3 * potency)));
        addToBot(new ApplyPowerAction(p,p, new MyFadingPower(p, 2)));
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new InvigoratingPotion();
    }
}
