package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.UseLightAction;
import nearlmod.powers.LightPower;
import nearlmod.stances.DefStance;

public class LightCard extends AbstractNearlCard {
    public static final String ID = "nearlmod:Light!";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 0;
    private static final int EXTRA_LIGHT = 2;

    public LightCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        switchedStance();
        selfRetain = true;
        exhaust = true;
        magicNumber = baseMagicNumber = EXTRA_LIGHT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
            addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) {
            addToBot(new UseLightAction(p));
        } else {
            addToBot(new UseLightAction(m));
        }
    }

    @Override
    public void switchedStance() {
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) this.target = CardTarget.SELF;
        else this.target = CardTarget.ENEMY;
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
