package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.UseLightAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;
import nearlmod.stances.DefStance;

public class MajestyLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:MajestyLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 0;
    private static final int LIGHT_ADD = 6;
    private static final int UPGRADE_PLUS_LIGHT = 3;

    public MajestyLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.BASIC, CardTarget.SELF);
        magicNumber = baseMagicNumber = LIGHT_ADD;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new UseLightAction(p, m));
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MajestyLight();
    }

    @Override
    public void applyPowers() {
        switchedStance();
        super.applyPowers();
    }

    @Override
    public void switchedStance() {
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) this.target = CardTarget.SELF;
        else this.target = CardTarget.ENEMY;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
