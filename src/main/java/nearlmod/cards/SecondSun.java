package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;

public class SecondSun extends AbstractNearlCard {
    public static final String ID = "nearlmod:SecondSun";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/secondsun.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SecondSun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = AbstractDungeon.player.getPower("nearlmod:LightPower");
        if (power == null) return;
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, power.amount), power.amount));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SecondSun();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
